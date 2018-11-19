package org.clever.security.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.IDCreateUtils;
import org.clever.common.utils.codec.DigestUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.jwt.jackson2.CleverSecurityJackson2Module;
import org.clever.security.jwt.model.JwtToken;
import org.junit.Test;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-16 17:15 <br/>
 */
@Slf4j
public class JwtTest {

    /**
     * 64个字符
     */
    private static String str = "0123456789012345678901234567890123456789012345678901234567890123";

    @Test
    public void t00() {
        byte[] key = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
        log.info("### {}", EncodeDecodeUtils.encodeHex(key));
    }

    @Test
    public void t01() {
        Key key = Keys.hmacShaKeyFor(str.getBytes());
        Map<String, Object> map = new HashMap<>();
        map.put("int", 1);
        map.put("boolean", true);
        map.put("string", "abc");

        DefaultClaims claims = new DefaultClaims();
        claims.setSubject("Joe");
        claims.setAudience("aud");
        claims.put("string", "abc");
        claims.put("int", 1);
        claims.put("boolean", true);
        String jws = Jwts.builder()
                .setHeader(map)
                .setClaims(claims)
                .claim("claim-name", "claim-value")
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        log.info("### {}", jws);

        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jws);
        log.info("### {}", claimsJws);
    }

    @Test
    public void t02() {
        String jws = "eyJib29sZWFuIjp0cnVlLCJzdHJpbmciOiJhYmMiLCJpbnQiOjEsImFsZyI6IkhTNTEyIn0.eyJzdWIiOiJKb2UiLCJhdWQiOiJhdWQiLCJzdHJpbmciOiJhYmMiLCJpbnQiOjEsImJvb2xlYW4iOnRydWV9.NATN4nq13WQ3PujFGmhFr1-t3QVcGzQ3ky_g0zMYpgEZp7A-w1Qh-1o4GvEdNv9RvFNaehH61rREwkQ9b2q1WA\n";
        Key key = Keys.hmacShaKeyFor(str.getBytes());
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jws);
        log.info("### {}", claimsJws);
    }

    @Test
    public void t03() {
        //创建Token令牌 - iss（签发者）, aud（接收方）, sub（面向的用户）,exp（过期时间戳）, iat（签发时间）
        DefaultClaims claims = new DefaultClaims();
        claims.setIssuer("clever-security-jwt");
        claims.setAudience("clever-*");
        claims.setSubject("lizhiwei");
        claims.setExpiration(new Date(System.currentTimeMillis() + 1000000000000L));
        claims.setIssuedAt(new Date());
        // 设置角色和权限
        claims.put("PermissionKey", new HashSet<String>() {{
            add("a01");
            add("a02");
        }});
        claims.put("RoleKey", new HashSet<String>(0));
        // 签名私钥
        Key key = Keys.hmacShaKeyFor(("lizhiwei" + str).getBytes());
        String jws = Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        log.info("### {}", jws);

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jws);
        log.info("### {}", claimsJws);
        log.info("### json -> {}", JacksonMapper.nonEmptyMapper().toJson(claimsJws));

        String[] strArray = jws.split("\\.");
        if (strArray.length != 3) {
            throw new BusinessException("Token格式不正确");
        }
        String payload = strArray[1];
        payload = new String(EncodeDecodeUtils.decodeBase64(payload));
        log.info("### {}", payload);
        claims = JacksonMapper.nonEmptyMapper().fromJson(payload, DefaultClaims.class);
        log.info("### {}", claims.getSubject());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void t04() {
        DefaultClaims claims = new DefaultClaims();
        claims.setIssuer("clever-security-jwt");
        claims.setAudience("clever-*");
        claims.setSubject("lizhiwei");
        claims.setExpiration(new Date(System.currentTimeMillis() + 1000000000000L));
        claims.setIssuedAt(new Date());
        // 设置角色和权限
        claims.put("PermissionKey", new HashSet<String>() {{
            add("a01");
            add("a02");
        }});
        claims.put("RoleKey", new HashSet<String>(0));
        // 签名私钥
        Key key = Keys.hmacShaKeyFor(("lizhiwei" + str).getBytes());
        String jws = Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        log.info("### {}", jws);

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jws);

        JwtToken jwtToken = new JwtToken();
        jwtToken.setToken(jws);
        jwtToken.setHeader(claimsJws.getHeader());
        jwtToken.setClaims(claimsJws.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//        objectMapper.setDateFormat();
//        objectMapper.setDefaultMergeable()
//        objectMapper.setDefaultPropertyInclusion()
//        objectMapper.setDefaultSetterInfo()
//        objectMapper.setDefaultVisibility()
        // 查看Spring的实现 SecurityJackson2Modules
//        List<Module> modules = SecurityJackson2Modules.getModules(getClass().getClassLoader());
        objectMapper.findAndRegisterModules();
//        objectMapper.registerModules(modules);
        objectMapper.registerModule(new CleverSecurityJackson2Module());
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        String json = new String(genericJackson2JsonRedisSerializer.serialize(jwtToken));
        log.info("### json -> {}", json);
        Object object = genericJackson2JsonRedisSerializer.deserialize(json.getBytes());
        log.info("### object -> {}", object);
    }

    @Test
    public void t05() {
        String str = "lizhiwei" + IDCreateUtils.shortUuid();
        byte[] key = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
        str = EncodeDecodeUtils.encodeHex(DigestUtils.sha1(str.getBytes(), key));
        log.info("### str -> {}", str);
        log.info("### str -> {}", IDCreateUtils.shortUuid());
    }
}
