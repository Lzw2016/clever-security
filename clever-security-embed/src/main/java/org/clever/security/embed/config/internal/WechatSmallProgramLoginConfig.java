package org.clever.security.embed.config.internal;

import lombok.Data;

import java.io.Serializable;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 13:08 <br/>
 */
@Data
public class WechatSmallProgramLoginConfig implements Serializable {
    /**
     * 是否启用手机号验证码登录
     */
    private boolean enable = false;
    /**
     * 小程序 appId
     */
    private String appId;
    /**
     * 小程序 appSecret
     */
    private String appSecret;
}
