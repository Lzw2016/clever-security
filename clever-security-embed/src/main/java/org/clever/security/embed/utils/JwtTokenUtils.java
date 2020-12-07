package org.clever.security.embed.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.DateTimeUtils;
import org.clever.common.utils.IDCreateUtils;
import org.clever.common.utils.SnowFlake;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.common.utils.tuples.TupleTow;
import org.clever.security.embed.authentication.login.AddJwtTokenExtData;
import org.clever.security.embed.config.internal.TokenConfig;
import org.clever.security.embed.exception.LoginInnerException;
import org.clever.security.embed.exception.ParserJwtTokenException;
import org.clever.security.model.UserInfo;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/02 21:32 <br/>
 */
@Slf4j
public class JwtTokenUtils {
    private static final String Secret_Key_Suffix = "0123456789012345678901234567890123456789012345678901234567890123";

    /**
     * 创建JWT-Token
     *
     * @param tokenConfig Token配置
     * @param userInfo    用户信息
     */
    public static TupleTow<String, Claims> createJwtToken(TokenConfig tokenConfig, UserInfo userInfo , List<AddJwtTokenExtData> addJwtTokenExtDataList) {
        // 获取当前时间戳
        long now = System.currentTimeMillis();
        // Token过期时间
        Date expiration = new Date(now + tokenConfig.getTokenValidity().toMillis());
        // 优化过期时间
        if (StringUtils.isNotBlank(tokenConfig.getHoursInDay())) {
            String date = DateTimeUtils.formatToString(expiration, DateTimeUtils.yyyy_MM_dd);
            try {
                expiration = DateTimeUtils.parseDate(date + " " + StringUtils.trim(tokenConfig.getHoursInDay()), DateTimeUtils.yyyy_MM_dd_HH_mm_ss);
            } catch (Throwable e) {
                log.warn("### TokenConfig.hoursInDay配置错误", e);
            }
            if (expiration.getTime() <= now) {
                expiration = DateTimeUtils.addDays(expiration, 1);
            }
        }
        //创建Token令牌 - iss（签发者）, aud（接收方）, sub（面向的用户）,exp（过期时间戳）, iat（签发时间）, jti（JWT ID）
        DefaultClaims claims = new DefaultClaims();
        claims.setIssuer(tokenConfig.getIssuer());
        claims.setAudience(tokenConfig.getAudience());
        claims.setSubject(userInfo.getUid());
        claims.setExpiration(expiration);
        claims.setIssuedAt(new Date());
        claims.setId(String.valueOf(SnowFlake.SNOW_FLAKE.nextId()));
        // 加入自定义信息
        if (addJwtTokenExtDataList != null && !addJwtTokenExtDataList.isEmpty()) {
            // addJwtTokenExtDataList = ListSortUtils.sort(addJwtTokenExtDataList);
            Map<String, Object> extData = new HashMap<>();
            for (AddJwtTokenExtData addJwtTokenExtData : addJwtTokenExtDataList) {
                extData = addJwtTokenExtData.addExtData(tokenConfig, userInfo, extData);
            }
            if (extData != null) {
                extData.forEach((key, value) -> {
                    if (claims.containsKey(key)) {
                        throw new LoginInnerException("设置Token扩展信息失败，key=[" + key + "]重复(不能覆盖内置数据)");
                    }
                    claims.put(key, value);
                });
            }
        }
        // 签名私钥
        Key key = getHmacShaKey(tokenConfig.getSecretKey(), userInfo.getUid());
        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        return TupleTow.creat(jwtToken, claims);
    }

    /**
     * 创建刷新Token
     *
     * @param userInfo 用户信息
     */
    public static String createRefreshToken(UserInfo userInfo) {
        return userInfo.getUid() + ":" + IDCreateUtils.shortUuid();
    }

    /**
     * 解析验证JWT-Token
     *
     * @param tokenConfig Token配置
     * @param jwtToken    JWT-Token字符串
     */
    public static Claims parserJwtToken(TokenConfig tokenConfig, String jwtToken) {
        String[] strArray = jwtToken.split("\\.");
        if (strArray.length != 3) {
            throw new MalformedJwtException("Token格式不正确");
        }
        // 解析获得签名私钥
        String payload = strArray[1];
        payload = new String(EncodeDecodeUtils.decodeBase64(payload));
        DefaultClaims claims = JacksonMapper.getInstance().fromJson(payload, DefaultClaims.class);
        String uid = claims.getSubject();
        Key key = getHmacShaKey(tokenConfig.getSecretKey(), uid);
        try {
            // 通过密钥验证Token
            return Jwts.parserBuilder()
                    // .requireIssuer(tokenConfig.getIssuer())
                    // .requireAudience(tokenConfig.getAudience())
                    // .requireSubject(uid)
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken).getBody();
        } catch (SignatureException e) {
            // 签名异常
            throw new ParserJwtTokenException("Token签名异常", e);
        } catch (MalformedJwtException e) {
            // JWT格式错误
            throw new ParserJwtTokenException("Token格式错误", e);
        } catch (ExpiredJwtException e) {
            // JWT过期
            throw new ParserJwtTokenException("TokenJWT过期", e);
        } catch (UnsupportedJwtException e) {
            // 不支持该JWT
            throw new ParserJwtTokenException("不支持该Token", e);
        } catch (JwtException e) {
            throw new ParserJwtTokenException("Token验证失败", e);
        } catch (IllegalArgumentException e) {
            // 参数错误异常
            throw new ParserJwtTokenException("Token参数错误异常", e);
        }
    }

    /**
     * 获取JWT-Token签名
     *
     * @param secretKey 签名配置
     * @param uid       用户UID
     */
    private static Key getHmacShaKey(String secretKey, String uid) {
        secretKey = secretKey + Secret_Key_Suffix;
        byte[] bytes = (StringUtils.left(uid, 8) + secretKey).getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }
}
