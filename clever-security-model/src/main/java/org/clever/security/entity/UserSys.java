package org.clever.security.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户-系统(UserSys)实体类
 *
 * @author lizw
 * @since 2018-09-16 21:24:44
 */
@Data
public class UserSys implements Serializable {
    private static final long serialVersionUID = -49478969546591808L;
    /** 登录名 */    
    private String username;
    
    /** 系统(或服务)名称 */    
    private String sysName;
    
    /** 创建时间 */    
    private Date createAt;
    
    /** 更新时间 */    
    private Date updateAt;
    
}