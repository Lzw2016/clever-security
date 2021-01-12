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
 * 创建时间：2020/12/14 20:58 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class ClearLoginFailedCountReq extends BaseRequest {
    /**
     * 域id
     */
    @NotNull(message = "域id不能为null")
    private Long domainId;
    /**
     * 用户id
     */
    @NotBlank(message = "用户id不能为null")
    private String uid;

    /**
     * 登录方式，1:用户名密码，2:手机号验证码，3:邮箱验证码，4:刷新token，5:微信小程序，6:扫码登录
     */
    @NotNull(message = "登录方式不能为null")
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
    private Integer loginType;

    public ClearLoginFailedCountReq(Long domainId) {
        this.domainId = domainId;
    }
}
