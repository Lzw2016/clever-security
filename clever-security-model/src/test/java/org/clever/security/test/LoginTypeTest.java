package org.clever.security.test;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.LoginType;
import org.junit.Test;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 15:52 <br/>
 */
@Slf4j
public class LoginTypeTest {

    @Test
    public void t01() {
        log.info("--> {}", LoginType.valueOf("LoginName_Password"));
    }
}
