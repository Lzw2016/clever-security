package org.clever.security.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.TimeZone;

/**
 * 应用启动类
 * Created by lzw on 2017/2/25.
 */
@Slf4j
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"org.clever.security.client"})
@EnableRedisHttpSession(
        cleanupCron = "0/3 * * * * *",
        maxInactiveIntervalInSeconds = 60,
        redisNamespace = "spring:session:demo",
        redisFlushMode = RedisFlushMode.ON_SAVE
)
@SpringBootApplication(scanBasePackages = {"org.clever"})
public class DemoStartApp {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        ApplicationContext ctx = SpringApplication.run(DemoStartApp.class, args);
        log.info("### 服务启动完成 === " + ctx);
    }
}
