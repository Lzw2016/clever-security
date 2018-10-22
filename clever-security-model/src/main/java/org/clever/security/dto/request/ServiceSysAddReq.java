package org.clever.security.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-22 20:41 <br/>
 */
@Data
public class ServiceSysAddReq implements Serializable {

    @ApiModelProperty("系统(或服务)名称")
    @NotBlank
    @Length(max = 127)
    private String sysName;

    @ApiModelProperty("全局的Session Redis前缀")
    @NotBlank
    @Length(max = 127)
    private String redisNameSpace;

    @ApiModelProperty("说明")
    @Length(max = 511)
    private String description;
}
