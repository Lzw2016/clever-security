package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-16 15:38 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class MatchesPasswordReq extends BaseRequest {
    /**
     * 原始密码
     */
    @NotBlank(message = "原始密码不能为空")
    private String rawPassword;
    /**
     * 加密后的密码
     */
    @NotBlank(message = "加密后的密码不能为空")
    private String encodedPassword;
}
