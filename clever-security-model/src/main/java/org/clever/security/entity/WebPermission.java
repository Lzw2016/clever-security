package org.clever.security.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * web权限表(permission子表)(WebPermission)实体类
 *
 * @author lizw
 * @since 2018-09-16 21:24:44
 */
@Data
public class WebPermission implements Serializable {
    private static final long serialVersionUID = 197256500272108557L;
    /** 主键id */    
    private Long id;
    
    /** 权限标识字符串 */    
    private String permissionStr;
    
    /** controller类名称 */    
    private String controllerClass;
    
    /** controller类的方法名称 */    
    private String controllerMethod;
    
    /** controller类的方法参数签名 */    
    private String controllerMethodParams;
    
    /** 资源url地址(只用作显示使用) */    
    private String resourcesUrl;
    
    /** 需要授权才允许访问，1：需要；2：不需要 */    
    private Integer needAuthorization;
    
    /** controller路由资源是否存在，0：不存在；1：存在 */    
    private Integer controllerExist;
    
    /** 创建时间 */    
    private Date createAt;
    
    /** 更新时间 */    
    private Date updateAt;
    
}