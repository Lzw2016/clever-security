package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;
import org.clever.common.validation.ValidIntegerStatus;
import org.clever.security.entity.EnumConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 16:35 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class VerifyLoginCaptchaReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 登录方式ID(1:用户名密码，2:手机号验证码，3:邮箱验证码，4:刷新令牌，5:微信小程序，6:扫码登录)
     */
    @ValidIntegerStatus(
            value = {
                    EnumConstant.UserLoginLog_LoginType_1,
                    EnumConstant.UserLoginLog_LoginType_2,
                    EnumConstant.UserLoginLog_LoginType_3,
                    EnumConstant.UserLoginLog_LoginType_4,
                    EnumConstant.UserLoginLog_LoginType_5,
                    EnumConstant.UserLoginLog_LoginType_6,
            },
            message = "不支持的登录方式值"
    )
    private int loginTypeId;
    /**
     * 登录唯一名称(查询用户条件)
     * <pre>
     *     1.用户名密码   -> loginName
     *     2.手机号验证码 -> telephone
     *     3.邮箱验证码   -> email
     * </pre>
     */
    @NotBlank(message = "登录名不能为空")
    private String loginUniqueName;

    /**
     * 登录失败多少次才需要验证码(小于等于0表示总是需要验证码)
     */
    private Integer needCaptchaByLoginFailedCount = 3;
    /**
     * 验证码
     */
    private String captcha;
    /**
     * 验证码签名(查询验证码条件)
     */
    private String captchaDigest;

    public VerifyLoginCaptchaReq(Long domainId) {
        this.domainId = domainId;
    }
}
