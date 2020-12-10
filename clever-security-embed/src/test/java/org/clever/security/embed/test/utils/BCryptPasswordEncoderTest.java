package org.clever.security.embed.test.utils;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.crypto.BCryptPasswordEncoder;
import org.junit.Test;

import java.security.SecureRandom;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/10 21:37 <br/>
 */
@Slf4j
public class BCryptPasswordEncoderTest {

    @Test
    public void t01() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
        String password = "123";
        String after = passwordEncoder.encode(password);
        log.info("-> {}", after);
        log.info("-> {}", passwordEncoder.encode(password));
        log.info("-> {}", passwordEncoder.encode(password));
        log.info("-> {}", passwordEncoder.matches(password, after));
        log.info("-> {}", passwordEncoder.matches(password, after));
        log.info("-> {}", passwordEncoder.matches(password, after));
    }
}
