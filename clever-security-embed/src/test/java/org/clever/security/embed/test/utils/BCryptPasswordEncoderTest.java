package org.clever.security.embed.test.utils;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.codec.CryptoUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.security.embed.crypto.AesPasswordEncoder;
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

    @Test
    public void t02() {
        log.info("-> {}", EncodeDecodeUtils.encodeHex(CryptoUtils.generateAesKey()));
        String key = "bb6de39cb39ebb1358adaa89a1f72842";
        String iv = "f254cdb4ff0f9224d01a82c66c95e940";
        AesPasswordEncoder passwordEncoder = new AesPasswordEncoder(key, iv);
        String password = "123";
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
