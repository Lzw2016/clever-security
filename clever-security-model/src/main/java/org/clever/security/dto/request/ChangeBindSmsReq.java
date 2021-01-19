package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

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
public class ChangeBindSmsReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;

    @NotBlank(message = "用户id不能为空")
    private String uid;

    @NotBlank(message = "密码不能为空")
    private String passWord;

    @NotBlank(message = "新手机号不能为空")
    @Pattern(regexp = "(?:0|86|\\+86)?1[3456789]\\d{9}", message = "手机号格式错误")
    private String telephone;

    public ChangeBindSmsReq(Long domainId) {
        this.domainId = domainId;
    }
}