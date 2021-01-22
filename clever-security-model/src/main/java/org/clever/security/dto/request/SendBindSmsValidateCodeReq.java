package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.response.BaseResponse;
import org.clever.security.PatternConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/01/18 15:13 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class SendBindSmsValidateCodeReq extends BaseResponse {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 图片验证码
     */
    private String captcha;
    /**
     * 验证码签名(校验验证码时需要)
     */
    private String captchaDigest;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = PatternConstant.Telephone_Pattern, message = "手机号格式错误")
    private String telephone;
    /**
     * 验证码过期时间
     */
    private int effectiveTimeMilli = 120 * 1000;
    /**
     * 一天发送短信验证码的最大数(小于等于0表示不限制)
     */
    private int maxSendNumInDay = 8;
}
