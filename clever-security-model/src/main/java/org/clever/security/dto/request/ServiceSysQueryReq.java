package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-25 20:09 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceSysQueryReq extends QueryByPage {

    @ApiModelProperty("系统(或服务)名称")
    private String sysName;

    @ApiModelProperty("登录类型，0：sesion-cookie，1：jwt-token")
    private Integer loginModel;
}
