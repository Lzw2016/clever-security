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
     * 创建时间戳(毫秒)
     */
    private Long createTime;

    /**
     * 有效时间(毫秒)
     */
    private Long effectiveTime;

    /**
     * 验证码文件图片签名
     */
    private String imageDigest;

    /**
     * 默认构造
     */
    public CaptchaInfo() {
        createTime = System.currentTimeMillis();
    }

    /**
     * Session 登录模式使用
     *
     * @param code          验证码
     * @param effectiveTime 有效时间(毫秒)
     */
    public CaptchaInfo(String code, Long effectiveTime) {
        this.code = code;
        this.createTime = System.currentTimeMillis();
        this.effectiveTime = effectiveTime;
    }

    /**
     * JWT Token 登录模式使用
     *
     * @param code          验证码
     * @param effectiveTime 有效时间(毫秒)
     * @param imageDigest   验证码文件图片签名
     */
    public CaptchaInfo(String code, Long effectiveTime, String imageDigest) {
        this.code = code;
        this.createTime = System.currentTimeMillis();
        this.effectiveTime = effectiveTime;
        this.imageDigest = imageDigest;
    }
}
