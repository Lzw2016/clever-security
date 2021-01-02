package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.clever.security.model.auth.ApiPermissionModel;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-02 11:41 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterApiPermissionReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;

    @Valid
    @NotEmpty(message = "API权限数据不能为空")
    private List<ApiPermissionModel> apiPermissionList;

    public RegisterApiPermissionReq(Long domainId) {
        this.domainId = domainId;
    }
}
