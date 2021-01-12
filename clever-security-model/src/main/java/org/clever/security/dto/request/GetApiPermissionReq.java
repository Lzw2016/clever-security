package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/14 19:13 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class GetApiPermissionReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;

    /**
     * controller类名称
     */
    @NotBlank(message = "controller类名称不能为空")
    private String className;

    /**
     * controller类的方法名称
     */
    @NotBlank(message = "controller类的方法名称不能为空")
    private String methodName;

    /**
     * controller类的方法参数签名
     */
    @NotBlank(message = "controller类的方法参数签名不能为空")
    private String methodParams;

    public GetApiPermissionReq(Long domainId) {
        this.domainId = domainId;
    }
}
