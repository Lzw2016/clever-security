package org.clever.security.embed.test.utils;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.IDCreateUtils;
import org.clever.common.utils.tuples.TupleTow;
import org.clever.security.embed.config.internal.TokenConfig;
import org.clever.security.embed.utils.JwtTokenUtils;
import org.clever.security.model.UserInfo;
import org.junit.Test;

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
}
