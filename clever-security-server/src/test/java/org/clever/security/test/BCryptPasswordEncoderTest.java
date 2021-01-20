package org.clever.security.test;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.codec.CryptoUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.security.crypto.AesPasswordEncoder;
import org.clever.security.crypto.BCryptPasswordEncoder;
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
        log.info("-> {}", passwordEncoder.matches("123456", "$2a$10$JXCYbQAabE5TQgIJOjumuu47SyYQTXIOmwfklEACHiQ71G1HdKLPC"));
    }

    @Test
    public void t02() {
        log.info("-> {}", EncodeDecodeUtils.encodeHex(CryptoUtils.generateAesKey()));
        String key = "b9049bb512f6b776835b9bf9e6c44c45";
        String iv = "47e2cf4f8ee69fd9d7f3cad475682df8";
        AesPasswordEncoder passwordEncoder = new AesPasswordEncoder(key, iv);
        String password = "112233";
        String after = passwordEncoder.encode(password);
        log.info("-> {}", after);
        log.info("-> {}", passwordEncoder.encode(password));
        log.info("-> {}", passwordEncoder.encode(password));
        log.info("-> {}", passwordEncoder.decode(after));
        log.info("-------------------------------------------------------");
        password = "1234567890123456";
        after = passwordEncoder.encode(password);
        log.info("-> {}", after);
        log.info("-> {}", passwordEncoder.decode(after));
    }
}
