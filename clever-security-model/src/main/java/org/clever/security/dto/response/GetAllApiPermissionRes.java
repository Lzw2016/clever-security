package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.response.BaseResponse;
import org.clever.security.model.auth.ApiPermissionEntity;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-02 19:32 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class GetAllApiPermissionRes extends BaseResponse {

    private List<ApiPermissionEntity> allApiPermissionList;
}
