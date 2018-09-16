package org.clever.security.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.TimeZone;

/**
 * 应用启动类
 * Created by lzw on 2017/2/25.
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"org.clever"})
public class DemoStartApp {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        ApplicationContext ctx = SpringApplication.run(DemoStartApp.class, args);
        log.info("### 服务启动完成 === " + ctx);
    }
}
