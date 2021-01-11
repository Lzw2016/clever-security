package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.clever.common.validation.ValidIntegerStatus;
import org.clever.security.entity.EnumConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-11 21:22 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddUserRegisterLogReq extends BaseRequest {
    /**
     * 注册的域id
     */
    @NotNull(message = "域id不能为null")
    private Long registerDomainId;

    /**
     * 注册IP
     */
    @NotBlank(message = "注册IP不能为空")
    private String registerIp;

    /**
     * 注册渠道，0:PC-Admin，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序
     */
    @ValidIntegerStatus(
            value = {
                    EnumConstant.User_RegisterChannel_0,
                    EnumConstant.User_RegisterChannel_1,
                    EnumConstant.User_RegisterChannel_2,
                    EnumConstant.User_RegisterChannel_3,
                    EnumConstant.User_RegisterChannel_4,
                    EnumConstant.User_RegisterChannel_5,
            },
            message = "不支持的注册渠道值"
    )
    private Integer registerChannel;

    /**
     * 注册类型，1:登录名注册，2:手机号注册，3:邮箱注册，4:微信小程序注册，
     */
    @ValidIntegerStatus(
            value = {
                    EnumConstant.UserRegisterLog_RegisterType_1,
                    EnumConstant.UserRegisterLog_RegisterType_2,
                    EnumConstant.UserRegisterLog_RegisterType_3,
                    EnumConstant.UserRegisterLog_RegisterType_4,
            },
            message = "不支持的注册类型值"
    )
    private Integer registerType;

    /**
     * 注册请求数据
     */
    private String requestData;

    /**
     * 注册结果，0:注册失败，1:注册成功且创建用户，2:注册成功仅关联到域
     */
    @NotNull(message = "注册结果不能为null")
    @ValidIntegerStatus(
            value = {
                    EnumConstant.UserRegisterLog_RequestResult_1,
                    EnumConstant.UserRegisterLog_RequestResult_2,
                    EnumConstant.UserRegisterLog_RequestResult_3,
            },
            message = "不支持的注册结果值"
    )
    private Integer requestResult;

    /**
     * 注册成功的用户id
     */
    private String registerUid;

    public AddUserRegisterLogReq(Long registerDomainId) {
        this.registerDomainId = registerDomainId;
    }
}
