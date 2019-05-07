package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-02 23:51 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQueryPageReq extends QueryByPage {

    @ApiModelProperty("角色名称(模糊匹配)")
    private String name;
}
