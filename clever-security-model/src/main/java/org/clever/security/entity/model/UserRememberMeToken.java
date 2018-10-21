package org.clever.security.entity.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-21 21:59 <br/>
 */
@Data
public class UserRememberMeToken implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 系统(或服务)名称
     */
    private String sysName;

    /**
     * token序列号
     */
    private String series;

    /**
     * 用户登录名
     */
    private String username;

    /**
     * token数据
     */
    private String token;

    /**
     * 最后使用时间
     */
    private Date lastUsed;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 主键id
     */
    private Long userId;

    /**
     * 用户类型，0：系统内建，1：外部系统用户
     */
    private Integer userType;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 帐号过期时间
     */
    private Date expiredTime;

    /**
     * 帐号是否锁定，0：未锁定；1：锁定
     */
    private Integer locked;

    /**
     * 是否启用，0：禁用；1：启用
     */
    private Integer enabled;

    /**
     * 说明
     */
    private String description;
}
