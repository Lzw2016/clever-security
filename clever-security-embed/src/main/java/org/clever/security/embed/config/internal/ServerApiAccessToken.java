package org.clever.security.embed.config.internal;

import lombok.Data;

import java.io.Serializable;

/**
 * 服务间免登陆Token访问配置
 * 作者： lzw<br/>
 * 创建时间：2019-05-16 11:07 <br/>
 */
@Data
public class ServerApiAccessToken implements Serializable {
    /**
     * 请求头Token名称
     */
    private String tokenName = "server-api-token";
    /**
     * 请求头Token值
     */
    private String tokenValue;
}
