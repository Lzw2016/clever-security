package org.clever.security.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 角色表(Role)实体类
 *
 * @author lizw
 * @since 2018-09-16 21:24:44
 */
@Data
public class Role implements Serializable {
    private static final long serialVersionUID = -72497893232578795L;
    /** 主键id */    
    private Long id;
    
    /** 角色名称 */    
    private String name;
    
    /** 角色说明 */    
    private String description;
    
    /** 创建时间 */    
    private Date createAt;
    
    /** 更新时间 */    
    private Date updateAt;
    
}