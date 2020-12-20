package org.clever.security.admin;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;

/**
 * 应用启动类
 * Created by lzw on 2017/2/25.
 */
@Slf4j
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"org.clever.security.third.client"})
@EnableTransactionManagement
@MapperScan("org.clever.security.mapper")
@SpringBootApplication(scanBasePackages = {"org.clever"})
public class StartApp {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        ApplicationContext ctx = SpringApplication.run(StartApp.class, args);
        log.info("### 服务启动完成 === " + ctx);
    }
}
