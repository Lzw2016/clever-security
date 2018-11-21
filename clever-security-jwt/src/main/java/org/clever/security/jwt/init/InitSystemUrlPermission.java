package org.clever.security.jwt.init;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.jwt.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 初始化系统的所有Url权限
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-03-17 11:24 <br/>
 */
@SuppressWarnings("Duplicates")
@Component
@Slf4j
public class InitSystemUrlPermission implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 是否初始化完成
     */
    private volatile boolean initFinish = false;
    private AtomicInteger count = new AtomicInteger(0);
    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private InitSystemUrlPermissionJob job;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        count.addAndGet(1);
        log.info("### [系统权限初始化] 当前Spring容器初始化次数 {} ", count.get());
        if (count.get() < securityConfig.getWaitSpringContextInitCount()) {
            log.info("### [系统权限初始化] 等待Spring容器初始化次数 {} ", count.get());
            return;
        }
        if (initFinish) {
            log.info("### [系统权限初始化] 已经初始化完成，跳过。");
            return;
        }
        if (StringUtils.isBlank(securityConfig.getSysName())) {
            throw new RuntimeException("系统名称未配置");
        }
        initFinish = true;
        log.info("### [系统权限初始化] 开始初始化...");
        job.execute();
    }
}
