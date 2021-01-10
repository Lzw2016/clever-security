package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-10 16:03 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SendEmailValidateCodeReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 图片验证码
     */
    private String captcha;
    /**
     * 登录验证码签名(校验验证码时需要)
     */
    private String captchaDigest;
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    /**
     * 验证码过期时间
     */
    private int effectiveTimeMilli = 300 * 1000;
    /**
     * 一天发送邮箱验证码的最大数(小于等于0表示不限制)
     */
    private int maxSendNumInDay = 32;
}
