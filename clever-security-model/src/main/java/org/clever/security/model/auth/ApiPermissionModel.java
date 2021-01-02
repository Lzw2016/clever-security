package org.clever.security.model.auth;

import lombok.Data;
import org.clever.common.validation.ValidIntegerStatus;
import org.clever.security.entity.EnumConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-02 10:50 <br/>
 */
@Data
public class ApiPermissionModel implements Serializable {
    /**
     * 权限唯一字符串标识
     */
    @NotBlank(message = "权限唯一字符串标识不能为空")
    private String strFlag;

    /**
     * 权限标题
     */
    @NotBlank(message = "权限标题不能为空")
    private String title;

    /**
     * 是否启用授权，0:不启用，1:启用
     */
    @NotNull(message = "是否启用授权不能为null")
    @ValidIntegerStatus(value = {EnumConstant.Permission_EnableAuth_0, EnumConstant.Permission_EnableAuth_1})
    private Integer enableAuth;

    /**
     * 权限说明
     */
    private String description;

    /**
     * controller类名称
     */
    @NotBlank(message = "controller类名称不能为空")
    private String className;

    /**
     * controller类的方法名称
     */
    @NotBlank(message = "controller类的方法名称不能为空")
    private String methodName;

    /**
     * controller类的方法参数签名
     */
    @NotBlank(message = "controller类的方法参数签名不能为空")
    private String methodParams;

    /**
     * API接口地址(只用作显示使用)
     */
    @NotBlank(message = "API接口地址不能为空")
    private String apiPath;
}
