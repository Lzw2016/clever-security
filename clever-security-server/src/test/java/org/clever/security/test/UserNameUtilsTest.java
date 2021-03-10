package org.clever.security.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/03/10 14:06 <br/>
 */
@Slf4j
public class UserNameUtilsTest {

    @Test
    public void t01() {
        for (int i = 0; i < 100; i++) {
            log.info("-> {}", generateLoginName());
        }
    }

    public static int generateLoginName() {
        final int minLength = 6;
        final int maxLength = 12;
        return minLength + (int) (Math.random() * (maxLength - minLength + 1));
    }
}
