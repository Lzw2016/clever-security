package org.clever.security.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 验证码(ValidateCode)实体类
 *
 * @author lizw
 * @since 2020-11-28 19:47:43
 */
@Data
public class ValidateCode implements Serializable {
    private static final long serialVersionUID = 757086153879042300L;
    /**
     * 记住我Token id(系统自动生成且不会变化)
     */
    private Long id;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 验证码
     */
    private String code;

    /**
     * 验证码类型，1:登录验证码，2:找回密码验证码，3:重置验证码
     */
    private Integer type;

    /**
     * 验证码过期时间
     */
    private Date expiredTime;

    /**
     * 验证码验证时间(使用时间)
     */
    private Date validateTime;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;
}