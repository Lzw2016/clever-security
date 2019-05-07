package org.clever.security.entity.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-21 22:11 <br/>
 */
@Data
public class UserLoginLogModel implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 系统(或服务)名称
     */
    private String sysName;

    /**
     * 用户登录名
     */
    private String username;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录IP
     */
    private String loginIp;

    /**
     * 登录的用户信息
     */
    private String authenticationInfo;

    /**
     * 登录类型，0：sesion-cookie，1：jwt-token
     */
    private Integer loginModel;

    /**
     * 登录SessionID
     */
    private String sessionId;

    /**
     * 登录状态，0：未知；1：已登录；2：登录已过期
     */
    private Integer loginState;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    // ----------------------------------------------------------------------------- User

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
