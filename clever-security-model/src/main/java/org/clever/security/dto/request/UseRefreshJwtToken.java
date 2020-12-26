package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * 作者：yz <br/>
 * 创建时间：2020-12-24 21:28 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UseRefreshJwtToken extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;

    /**
     * JWT-Token id(系统自动生成且不会变化)
     */
    @NotNull(message = "JWT-Token id不能为null")
    private Long id;

    /**
     * 使用Refresh-JWT-Token 生成的jwt-token-id(系统自动生成且不会变化)
     */
    @NotNull(message = "JWT-Token id不能为null")
    private Long refreshCreateTokenId;

    public UseRefreshJwtToken(Long domainId) {
        this.domainId = domainId;
    }
}
