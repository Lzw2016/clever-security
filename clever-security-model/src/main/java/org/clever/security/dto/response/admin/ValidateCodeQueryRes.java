package org.clever.security.dto.response.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/18 14:10 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ValidateCodeQueryRes extends BaseResponse {
    /**
     * 验证码 id
     */
    private Long id;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * 用户id(触发生成验证码的用户)
     */
    private String uid;

    /**
     * 验证码
     */
    private String code;

    /**
     * 验证码签名
     */
    private String digest;

    /**
     * 验证码类型
     */
    private Integer type;

    /**
     * 验证码发送渠道，0:不需要发送，1:短信，2:email
     */
    private Integer sendChannel;

    /**
     * 发送目标手机号或邮箱
     */
    private String sendTarget;

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

    // --------------------------------------------------------------------------------------------------------- domain

    /**
     * 域名称
     */
    private String domainName;

    // --------------------------------------------------------------------------------------------------------- user

    /**
     * 用户登录名(允许修改)
     */
    private String loginName;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;
}
