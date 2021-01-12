package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 16:35 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class GetLoginFailedCountAndCaptchaReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 验证码签名(查询验证码条件)
     */
    private String captchaDigest;
    /**
     * 登录方式ID(1:用户名密码，2:手机号验证码，3:邮箱验证码，4:刷新令牌，5:微信小程序，6:扫码登录)
     */
    private int loginTypeId;
    /**
     * 登录唯一名称(查询用户条件)
     * <pre>
     *     1.用户名密码   -> loginName
     *     2.手机号验证码 -> telephone
     *     3.邮箱验证码   -> email
     * </pre>
     */
    private String loginUniqueName;

    public GetLoginFailedCountAndCaptchaReq(Long domainId) {
        this.domainId = domainId;
    }
}
