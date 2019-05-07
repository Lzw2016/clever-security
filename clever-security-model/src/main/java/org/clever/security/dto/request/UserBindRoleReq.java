package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-03 21:53 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserBindRoleReq extends BaseRequest {

    @ApiModelProperty("用户名集合")
    @Size(min = 1, max = 100)
    @NotNull
    private List<String> usernameList;

    @ApiModelProperty("角色集合")
    @NotNull
    private List<String> roleNameList;
}
