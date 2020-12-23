package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.clever.common.validation.ValidIntegerStatus;
import org.clever.security.entity.EnumConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/14 19:36 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddUserLoginLogReq extends BaseRequest {
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
     * 登录时间
     */
    @NotNull(message = "登录时间不能为null")
    private Date loginTime;

    /**
     * 登录IP
     */
    @NotBlank(message = "登录IP不能为null")
    private String loginIp;

    /**
     * 登录渠道，0:PC-Admin，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序
     */
    private Integer loginChannel;

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

    /**
     * 登录状态，0:登录失败，1:登录成功
     */
    @NotNull(message = "登录状态不能为null")
    @ValidIntegerStatus(
            value = {EnumConstant.UserLoginLog_LoginState_0, EnumConstant.UserLoginLog_LoginState_1},
            message = "不支持的登录状态值"
    )
    private Integer loginState;

    /**
     * 登录请求数据
     */
    private String requestData;

    /**
     * JWT-Token id
     */
    private Long jwtTokenId;

    public AddUserLoginLogReq(Long domainId) {
        this.domainId = domainId;
    }
}
