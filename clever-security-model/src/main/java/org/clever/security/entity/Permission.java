package org.clever.security.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 权限表(Permission)实体类
 *
 * @author lizw
 * @since 2018-09-16 21:24:42
 */
@Data
public class Permission implements Serializable {
    private static final long serialVersionUID = 908890037496066530L;
    /** 主键id */    
    private Long id;
    
    /** 系统(或服务)名称 */    
    private String sysName;
    
    /** 权限标题 */    
    private String title;
    
    /** 唯一权限标识字符串 */    
    private String permissionStr;
    
    /** 权限类型，1:web资源权限, 2:菜单权限，3:ui权限，...... */    
    private Integer resourcesType;
    
    /** 权限说明 */    
    private String description;
    
    /** 创建时间 */    
    private Date createAt;
    
    /** 更新时间 */    
    private Date updateAt;
    
}