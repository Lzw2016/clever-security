package org.clever.security.test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.junit.Test;

import java.security.Key;
import java.util.HashMap;
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
}
