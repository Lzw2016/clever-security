package org.clever.security.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户登录日志(UserLoginLog)实体类
 *
 * @author lizw
 * @since 2018-09-23 19:55:43
 */
@Data
public class UserLoginLog implements Serializable {
    private static final long serialVersionUID = -11162979906700377L;
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

}