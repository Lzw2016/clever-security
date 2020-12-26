package org.clever.security.embed.test.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.IDCreateUtils;
import org.clever.common.utils.tuples.TupleTow;
import org.clever.security.embed.config.internal.TokenConfig;
import org.clever.security.embed.exception.ParserJwtTokenException;
import org.clever.security.embed.utils.JwtTokenUtils;
import org.clever.security.model.UserInfo;
import org.junit.Test;

import java.time.Duration;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/02 22:19 <br/>
 */
@Slf4j
public class JwtTokenUtilsTest {

    @Test
    public void t01() {
        TokenConfig tokenConfig = new TokenConfig();
        UserInfo userInfo = new UserInfo();
        userInfo.setUid("lizw" + IDCreateUtils.uuid());
        userInfo.setLoginName("lizw");
        TupleTow<String, Claims> tokenInfo = JwtTokenUtils.createJwtToken(tokenConfig, userInfo, null);
        log.info("jwtToken -> {}", tokenInfo.getValue1());

        Claims claims = JwtTokenUtils.parserJwtToken(tokenConfig, tokenInfo.getValue1());
        log.info("Body     -> {}", claims);
    }

    @Test
    public void t02() {
        TokenConfig tokenConfig = new TokenConfig();
        tokenConfig.setTokenValidity(Duration.ofDays(-7));
        UserInfo userInfo = new UserInfo();
        userInfo.setUid("lizw" + IDCreateUtils.uuid());
        userInfo.setLoginName("lizw");
        TupleTow<String, Claims> tokenInfo = JwtTokenUtils.createJwtToken(tokenConfig, userInfo, null);
        log.info("jwtToken -> {}", tokenInfo.getValue1());

        try {
            Claims claims = JwtTokenUtils.parserJwtToken(tokenConfig, tokenInfo.getValue1());
            log.info("Body     -> {}", claims);
        } catch (ParserJwtTokenException e) {
            if (!(e.getCause() instanceof ExpiredJwtException)) {
                throw e;
            }
            log.info("--> ", e);
        }

        try {
            String token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJjbGV2ZXItc2VjdXJpdHktand0IiwiYXVkIjoiY2xldmVyLSoiLCJzdWIiOiJsaXp3M2EyN2Q3MTUtYjkxMS00NGZlLThjMjctN2RmZjI4YzNhYWE0IiwiZXhwIjoxNjA4NDA3MTAwLCJpYXQiOjE2MDg5ODAxMjAsImp0aSI6IjU0MDI4MzYyOTQ2MTMwNzM5MiJ9.k-lfly6IqNP2pgxU12amr437HEHMgQ29q6v6pts9javVJmNsLxTEuZCSyFj63KD76e6N3bsR57hNNgURVZ5hJg";
            Claims claims = JwtTokenUtils.parserJwtToken(tokenConfig, token);
            log.info("Body     -> {}", claims);
        } catch (ParserJwtTokenException e) {
            if (!(e.getCause() instanceof ExpiredJwtException)) {
                throw e;
            }
            log.info("--> ", e.getCause());
        }
    }
}
