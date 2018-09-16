package org.clever.security.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户-角色(UserRole)实体类
 *
 * @author lizw
 * @since 2018-09-16 21:24:44
 */
@Data
public class UserRole implements Serializable {
    private static final long serialVersionUID = -79782786523606049L;
    /** 登录名 */    
    private String username;
    
    /** 角色名称 */    
    private String roleName;
    
    /** 创建时间 */    
    private Date createAt;
    
    /** 更新时间 */    
    private Date updateAt;
    
}