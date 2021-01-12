package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.response.BaseResponse;
import org.clever.security.model.auth.ApiPermissionModel;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-02 11:42 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterApiPermissionRes extends BaseResponse {
    /**
     * 新增的权限信息
     */
    private List<ApiPermissionModel> addPermissionList;

    /**
     * 不存在的权限信息
     */
    private List<ApiPermissionModel> notExistPermissionList;
}
