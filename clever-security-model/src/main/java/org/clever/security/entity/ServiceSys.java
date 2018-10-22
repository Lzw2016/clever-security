package org.clever.security.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 服务系统(ServiceSys)实体类
 *
 * @author lizw
 * @since 2018-10-22 15:22:30
 */
@Data
public class ServiceSys implements Serializable {
    private static final long serialVersionUID = 178234483743559404L;
    /** 主键id */    
    private Long id;
    
    /** 系统(或服务)名称 */    
    private String sysName;
    
    /** 全局的Session Redis前缀 */    
    private String redisNameSpace;
    
    /** 说明 */    
    private String description;
    
    /** 创建时间 */    
    private Date createAt;
    
    /** 更新时间 */    
    private Date updateAt;
    
}