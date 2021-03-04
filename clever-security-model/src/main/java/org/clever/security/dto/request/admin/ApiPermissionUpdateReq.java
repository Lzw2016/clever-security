package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/16 17:05 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiPermissionUpdateReq extends BaseRequest {
    /**
     * api id(系统自动生成且不会变化)
     */
    @NotNull(message = "API权限id不能为空")
    private Long id;

    /**
     * controller类名称
     */
    private String className;

    /**
     * controller类的方法名称
     */
    private String methodName;

    /**
     * controller类的方法参数签名
     */
    private String methodParams;

    /**
     * API接口地址(只用作显示使用)
     */
    private String apiPath;
}
