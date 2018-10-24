package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import java.util.Date;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-21 22:04 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RememberMeTokenQueryReq extends BaseRequest {

    @ApiModelProperty("用户登录名")
    private String username;

    @ApiModelProperty("系统名称")
    private String sysName;

    @ApiModelProperty("最后使用时间 - 开始")
    private Date lastUsedStart;

    @ApiModelProperty("最后使用时间 - 结束")
    private Date lastUsedEnd;

    @ApiModelProperty("手机号")
    private String telephone;

    @ApiModelProperty("邮箱")
    private String email;
}
