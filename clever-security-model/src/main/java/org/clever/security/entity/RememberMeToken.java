package org.clever.security.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * “记住我”功能的token(RememberMeToken)实体类
 *
 * @author lizw
 * @since 2018-09-21 20:10:31
 */
@Data
public class RememberMeToken implements Serializable {
    private static final long serialVersionUID = -57228137236940071L;
    /** 主键id */    
    private Long id;

    /** 系统(或服务)名称 */
    private String sysName;

    /** token序列号 */    
    private String series;
    
    /** 用户登录名 */    
    private String username;
    
    /** token数据 */    
    private String token;
    
    /** 最后使用时间 */    
    private Date lastUsed;
    
    /** 创建时间 */    
    private Date createAt;
    
    /** 更新时间 */    
    private Date updateAt;
    
}