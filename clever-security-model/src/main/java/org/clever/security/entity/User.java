package org.clever.security.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表(User)实体类
 *
 * @author lizw
 * @since 2018-09-16 21:24:44
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -73828129880873228L;
    /** 主键id */
    private Long id;

    /** 登录名 */
    private String username;

    /** 密码 */
    private String password;

    /** 用户类型，0：系统内建，1：外部系统用户 */
    private Integer userType;

    /** 手机号 */
    private String telephone;

    /** 邮箱 */
    private String email;

    /** 帐号是否锁定，0：未锁定；1：锁定 */
    private Integer locked;

    /** 是否启用，0：禁用；1：启用 */
    private Integer enabled;

    /** 说明 */
    private String description;

    /** 创建时间 */
    private Date createAt;

    /** 更新时间 */
    private Date updateAt;

}