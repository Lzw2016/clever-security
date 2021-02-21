package org.clever.security.config;

import lombok.Data;
import org.clever.security.Constant;
import org.clever.security.config.internal.ClearLogDataConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/20 23:04 <br/>
 */
@ConfigurationProperties(prefix = Constant.ConfigPrefix)
@Data
public class SecurityServerConfig {
    /**
     * 清理历史日志数据配置
     */
    @NestedConfigurationProperty
    private ClearLogDataConfig clearLogDataConfig = new ClearLogDataConfig();
}
