package org.clever.security.embed.config.internal;

import lombok.Data;

import java.io.Serializable;

/**
 * 服务间免登陆Token访问配置
 * 作者： lzw<br/>
 * 创建时间：2019-05-16 11:07 <br/>
 */
@Data
public class ServerAccessConfig implements Serializable {
    /**
     * 是否启用动态配置
     */
    private boolean dynamic = true;
    /**
     * 请求头Token名称
     */
    private String tokenName = "server-access-token";
    /**
     * 请求头Token值
     */
    private String tokenValue;
}
