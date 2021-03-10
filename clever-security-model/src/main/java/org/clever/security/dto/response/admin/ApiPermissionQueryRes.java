package org.clever.security.dto.response.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/03/10 22:07 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiPermissionQueryRes extends BaseResponse {
    // --------------------------------------------------------------------------------------------------------- ApiPermission

    /**
     * api id(系统自动生成且不会变化)
     */
    private Long id;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * 权限id
     */
    private Long permissionId;

    /**
     * API标题
     */
    private String title;

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

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    // --------------------------------------------------------------------------------------------------------- permission

    /**
     * 权限唯一字符串标识
     */
    private String strFlag;

    /**
     * 权限类型，1:API权限，2:菜单权限，3:页面UI权限
     */
    private Integer permissionType;

    /**
     * 是否启用授权，0:不启用，1:启用
     */
    private Integer enabled;

    /**
     * 权限说明
     */
    private String description;

    // --------------------------------------------------------------------------------------------------------- domain

    /**
     * 域名称
     */
    private String domainName;
}
