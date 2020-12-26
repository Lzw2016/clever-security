package org.clever.security.embed.test.utils;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.DateTimeUtils;
import org.junit.Test;

import java.time.Duration;
import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/26 13:17 <br/>
 */
@Slf4j
public class DateTimeUtilsTest {

    @Test
    public void t01() {
        Date date = new Date();
        log.debug("-> {}", DateTimeUtils.formatToString(date));
        Duration refreshTokenValidity = Duration.ofDays(30);
        date = new Date(date.getTime() + refreshTokenValidity.toMillis());
        log.debug("-> {}", DateTimeUtils.formatToString(date));

        log.debug("-> {} | {}", new Date().getTime(), System.currentTimeMillis());
    }
}
