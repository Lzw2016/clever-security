package org.clever.security.config.model;

import lombok.Data;

/**
 * 服务间免登陆Token访问配置
 * 作者： lzw<br/>
 * 创建时间：2019-05-16 11:07 <br/>
 */
@Data
public class ServerApiAccessToken {

    /**
     * 请求头Token名称
     */
    private String tokenName = "ServerApiToken";

    /**
     * 请求头Token值
     */
    private String tokenValue;
}
