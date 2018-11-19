package org.clever.security.jwt.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.IDCreateUtils;
import org.clever.common.utils.SnowFlake;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.jwt.config.SecurityConfig;
import org.clever.security.jwt.model.JwtToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-16 16:45 <br/>
 */
@Component
@Slf4j
public class JWTTokenService {

    private static final String PermissionKey = "permissions";
    private static final String RoleKey = "roles";
    private static final String SecretKeySuffix = "0123456789012345678901234567890123456789012345678901234567890123";

    /**
     * 签名密钥
     */
    private final String secretKey;

    /**
     * Token有效时间(默认)
     */
    private final long tokenValidityInMilliseconds;

    /**
     * Token记住我有效时间(记住我)
     */
    private final long tokenValidityInMillisecondsForRememberMe;

    protected JWTTokenService(SecurityConfig securityConfig) {
        SecurityConfig.TokenConfig tokenConfig = securityConfig.getTokenConfig();
        if (tokenConfig == null) {
            throw new BusinessException("未配置TokenConfig");
        }
        this.secretKey = tokenConfig.getSecretKey() + SecretKeySuffix;
        this.tokenValidityInMilliseconds = tokenConfig.getTokenValidity().toMillis();
        this.tokenValidityInMillisecondsForRememberMe = tokenConfig.getTokenValidityForRememberMe().toMillis();
    }

    /**
     * 创建Token，并将Token写入Redis
     *
     * @param authentication 授权信息
     * @param rememberMe     使用记住我
     */
    public JwtToken createToken(Authentication authentication, boolean rememberMe) {
        // 用户权限信息
        Set<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        //获取当前时间戳
        long now = System.currentTimeMillis();
        //存放过期时间
        Date expiration;
        if (rememberMe) {
            expiration = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            expiration = new Date(now + this.tokenValidityInMilliseconds);
        }
        //创建Token令牌 - iss（签发者）, aud（接收方）, sub（面向的用户）,exp（过期时间戳）, iat（签发时间）, jti（JWT ID）
        DefaultClaims claims = new DefaultClaims();
        claims.setIssuer("clever-security-jwt");
        claims.setAudience("clever-*");
        claims.setSubject(authentication.getName());
        claims.setExpiration(expiration);
        claims.setIssuedAt(new Date());
        claims.setId(String.valueOf(SnowFlake.SNOW_FLAKE.nextId()));
        // 设置刷新令牌
        claims.put(GenerateKeyService.JwtTokenRefreshKey, IDCreateUtils.shortUuid());
        // 设置角色和权限
        claims.put(PermissionKey, authorities);
        // TODO 设置权限
        claims.put(RoleKey, new HashSet<String>(0));
        // 签名私钥
        Key key = Keys.hmacShaKeyFor((authentication.getName() + secretKey).getBytes());
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        // 构建返回数据
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        JwtToken jwtToken = new JwtToken();
        jwtToken.setToken(token);
        jwtToken.setHeader(claimsJws.getHeader());
        jwtToken.setClaims(claimsJws.getBody());
        return jwtToken;
    }

    /**
     * 校验Token，校验失败抛出异常
     */
    public Jws<Claims> getClaimsJws(String token) {
        String[] strArray = token.split("\\.");
        if (strArray.length != 3) {
            throw new MalformedJwtException("Token格式不正确");
        }
        // 解析获得签名私钥
        String payload = strArray[1];
        payload = new String(EncodeDecodeUtils.decodeBase64(payload));
        DefaultClaims claims = JacksonMapper.nonEmptyMapper().fromJson(payload, DefaultClaims.class);
        Key key = Keys.hmacShaKeyFor((claims.getSubject() + secretKey).getBytes());
        try {
            //通过密钥验证Token
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        } catch (SignatureException e) {
            // 签名异常
            throw new BusinessException("Token签名异常", e);
        } catch (MalformedJwtException e) {
            // JWT格式错误
            throw new BusinessException("Token格式错误", e);
        } catch (ExpiredJwtException e) {
            // JWT过期
            throw new BusinessException("TokenJWT过期", e);
        } catch (UnsupportedJwtException e) {
            // 不支持该JWT
            throw new BusinessException("不支持该Token", e);
        } catch (IllegalArgumentException e) {
            // 参数错误异常
            throw new BusinessException("Token参数错误异常", e);
        }
    }
}
