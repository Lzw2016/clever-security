package org.clever.security.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户安全上下文(缓存表)(UserSecurityContext)实体类
 *
 * @author lizw
 * @since 2020-12-13 14:33:33
 */
@Data
public class UserSecurityContext implements Serializable {
    private static final long serialVersionUID = -26755618907767288L;
    /**
     * id(系统自动生成且不会变化)
     */
    private Long id;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 用户安全信息(安全上下文)
     */
    private String securityContext;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

}