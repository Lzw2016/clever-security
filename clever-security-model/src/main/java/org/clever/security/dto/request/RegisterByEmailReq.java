package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/11 14:54 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterByEmailReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 注册渠道，0:PC-Admin，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序
     */
    private String registerChannel;
    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;

    public RegisterByEmailReq(Long domainId) {
        this.domainId = domainId;
    }
}
