package org.clever.security.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-03 21:54 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserBindRoleRes extends BaseResponse {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("角色集合")
    private List<String> roleNameList;
}
