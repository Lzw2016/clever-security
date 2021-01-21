package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;
import org.clever.security.PatternConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/11 14:28 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterByLoginNameReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 注册渠道，0:PC-Admin，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序
     */
    private String registerChannel;
    /**
     * 用户登录名(允许修改)
     */
    @NotBlank(message = "用户登录名不能为空")
    @Pattern(regexp = PatternConstant.LoginName_Pattern, message = "用户名只能由“字母、数字、下划线、中划线”组成，且长度在4~32个字符范围内")
    private String loginName;
    /**
     * 密码
     */
    @NotBlank(message = "登录密码不能为空")
    @Size(min = 6, max = 63, message = "密码长度在6~63个字符之间")
    private String password;

    public RegisterByLoginNameReq(Long domainId) {
        this.domainId = domainId;
    }
}
