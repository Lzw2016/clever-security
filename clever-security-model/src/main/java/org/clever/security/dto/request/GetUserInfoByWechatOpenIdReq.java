package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 20:29 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class GetUserInfoByWechatOpenIdReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    @NotBlank(message = "微信openId不能为空")
    private String openId;

    /**
     * 微信unionId
     */
    private String unionId;

    public GetUserInfoByWechatOpenIdReq(Long domainId) {
        this.domainId = domainId;
    }
}
