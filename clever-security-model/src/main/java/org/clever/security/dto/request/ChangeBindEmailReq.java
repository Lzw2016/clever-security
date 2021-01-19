package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/01/18 15:13 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class ChangeBindEmailReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;

    @NotBlank(message = "用户id不能为空")
    private String uid;

    @NotBlank(message = "密码不能为空")
    private String passWord;

    @NotBlank(message = "新邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    public ChangeBindEmailReq(Long domainId) {
        this.domainId = domainId;
    }
}