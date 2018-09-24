package org.clever.security.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 验证码信息
 * 作者： lzw<br/>
 * 创建时间：2018-09-19 21:34 <br/>
 */
@Data
public class CaptchaInfo implements Serializable {

    /**
     * 验证码
     */
    private String code;

    /**
     * 创建时间戳
     */
    private Long createTime;

    /**
     * 有效时间(毫秒)
     */
    private Long effectiveTime;

    public CaptchaInfo() {
        createTime = System.currentTimeMillis();
    }

    public CaptchaInfo(String code, Long effectiveTime) {
        this.code = code;
        this.createTime = System.currentTimeMillis();
        this.effectiveTime = effectiveTime;
    }
}
