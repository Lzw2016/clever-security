package org.clever.security.admin.config;

import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.server.config.CustomPaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/16 16:08 <br/>
 */
@Configuration
@Slf4j
public class GlobalBeanDefinition {
    /**
     * 分页插件
     */
    @Bean
    public CustomPaginationInterceptor paginationInterceptor() {
        return new CustomPaginationInterceptor();
    }

    /**
     * 执行分析插件<br />
     * 作用是分析 处理 DELETE UPDATE 语句
     * 防止小白或者恶意 delete update 全表操作！
     */
    @Bean
    @Profile({"dev", "test"})
    public SqlExplainInterceptor sqlExplainInterceptor() {
        return new SqlExplainInterceptor();
    }
}
