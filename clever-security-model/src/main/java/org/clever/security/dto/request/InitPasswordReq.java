package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/18 20:07 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class InitPasswordReq extends BaseRequest {
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 63, message = "密码长度在6~63个字符之间")
    private String InitPassword;

    @NotBlank(message = "用户id不能为空")
    private String uid;
}
