package org.clever.security.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

/**
 * 作者： lzw<br/>
 * 创建时间：2017-12-04 10:37 <br/>
 */
@Configuration
@Slf4j
public class BeanConfiguration {

    /**
     * 密码处理
     */
    @Bean
    protected BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        paginationInterceptor.setSqlParser()
//        paginationInterceptor.setDialectClazz()
//        paginationInterceptor.setOverflow()
//        paginationInterceptor.setProperties();
        return paginationInterceptor;
    }

    /**
     * 乐观锁插件<br />
     * 取出记录时，获取当前version <br />
     * 更新时，带上这个version <br />
     * 执行更新时， set version = yourVersion+1 where version = yourVersion <br />
     * 如果version不对，就更新失败 <br />
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

//    /**
//     * 逻辑删除<br />
//     */
//    @Bean
//    public ISqlInjector sqlInjector() {
//        return new LogicSqlInjector();
//    }

    /**
     * SQL执行效率插件
     */
    @Bean
    @Profile({"dev", "test"})
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
//        performanceInterceptor.setFormat(true);
//        performanceInterceptor.setMaxTime();
//        performanceInterceptor.setWriteInLog();
        return performanceInterceptor;
    }

    /**
     * 执行分析插件<br />
     * SQL 执行分析拦截器【 目前只支持 MYSQL-5.6.3 以上版本 】
     * 作用是分析 处理 DELETE UPDATE 语句
     * 防止小白或者恶意 delete update 全表操作！
     */
    @Bean
    @Profile({"dev", "test"})
    public SqlExplainInterceptor sqlExplainInterceptor() {
        SqlExplainInterceptor sqlExplainInterceptor = new SqlExplainInterceptor();
//        sqlExplainInterceptor.stopProceed
        return sqlExplainInterceptor;
    }
}
