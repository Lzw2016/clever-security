package org.clever.security.model.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-02 14:10 <br/>
 */
@Data
public class ApiPermissionEntity implements Serializable {
    /**
     * 权限id(系统自动生成且不会变化)
     */
    private Long permissionId;

    /**
     * 上级权限id
     */
    private Long parentPermissionId;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * 权限唯一字符串标识
     */
    private String strFlag;

    /**
     * 权限标题
     */
    private String title;

    /**
     * 权限类型，1:API权限, 2:菜单权限，3:UI组件权限
     */
    private Integer resourcesType;

    /**
     * 是否启用授权，0:不启用，1:启用
     */
    private Integer enabled;

    /**
     * 权限说明
     */
    private String description;

    /**
     * API权限id(系统自动生成且不会变化)
     */
    private Long apiPermissionId;

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

    /**
     * API接口是否存在，0：不存在；1：存在
     */
    private Integer apiExist;
}
