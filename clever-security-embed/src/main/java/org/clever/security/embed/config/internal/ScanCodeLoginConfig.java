package org.clever.security.embed.config.internal;

import lombok.Data;

import java.io.Serializable;
import java.time.Duration;

/**
 * 扫码登录配置
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 11:08 <br/>
 */
@Data
public class ScanCodeLoginConfig implements Serializable {
    /**
     * 是否启用邮箱验证码登录
     */
    private boolean enable = false;
    /**
     * 获取扫码登录二维码(请求Path)
     */
    private String getScanCodeLoginPath = "/login/scan_code/get";
    /**
     * 扫码请求Path
     */
    private String scanCodePath = "/login/scan_code/scan";
    /**
     * 确认登录请求Path
     */
    private String confirmLoginPath = "/login/scan_code/confirm";
    /**
     * 登录二维码状态Path
     */
    private String scanCodeStatePath = "/login/scan_code/state";

    /**
     * 扫码登录二维码有效时间(生成二维码 -> 扫码请求时间，默认60秒)
     */
    private Duration expiredTime = Duration.ofSeconds(60);
    /**
     * 确认登录过期时间(扫码二维码 -> 确认登录时间，默认30秒)
     */
    private Duration confirmExpiredTime = Duration.ofSeconds(30);
    /**
     * 获取登录JWT-Token过期时间(确认登录 -> 获取登录Token时间，默认30秒)
     */
    private Duration getTokenExpiredTime = Duration.ofSeconds(30);
}
