package org.clever.security.embed.test.utils;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.utils.AesUtils;
import org.junit.Test;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/12 22:59 <br/>
 */
@Slf4j
public class AesUtilsTest {
    @Test
    public void t01() {
        log.info("---> {}", AesUtils.decode("b9049bb512f6b776835b9bf9e6c44c45", "47e2cf4f8ee69fd9d7f3cad475682df8", "aKreyULsRdIBG3Iy5URaGw=="));
    }
}
