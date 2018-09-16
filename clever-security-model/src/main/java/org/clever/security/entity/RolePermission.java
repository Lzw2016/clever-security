package org.clever.security.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 角色-权限(RolePermission)实体类
 *
 * @author lizw
 * @since 2018-09-16 21:24:44
 */
@Data
public class RolePermission implements Serializable {
    private static final long serialVersionUID = 148599893038494346L;
    /** 角色名称 */    
    private String roleName;
    
    /** 资源访问所需要的权限标识字符串 */    
    private String permissionStr;
    
    /** 创建时间 */    
    private Date createAt;
    
    /** 更新时间 */    
    private Date updateAt;
    
}