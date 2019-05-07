package org.clever.security.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-12 16:21 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ForcedOfflineRes extends BaseResponse {

    @ApiModelProperty("删除Session数")
    private int count = 0;

    @ApiModelProperty("用户名")
    private String sysName;

    @ApiModelProperty("系统名")
    private String username;
}
