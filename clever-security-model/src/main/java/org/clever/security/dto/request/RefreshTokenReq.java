package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-19 21:23 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RefreshTokenReq extends BaseRequest {

    /**
     * JWT 刷新 Token 字符串
     */
    @NotBlank
    private String refreshToken;
}
