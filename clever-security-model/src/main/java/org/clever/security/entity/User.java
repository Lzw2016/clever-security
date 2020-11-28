package org.clever.security.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表(User)实体类
 *
 * @author lizw
 * @since 2020-11-28 19:47:41
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 732223364326386263L;
    /**
     * 用户id(系统自动生成且不会变化)
     */
    private String uid;

    /**
     * 用户登录名(允许修改)
     */
    private String loginName;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 帐号过期时间(空表示永不过期)
     */
    private Date expiredTime;

    /**
     * 是否启用，0:禁用，1:启用
     */
    private Integer enabled;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户注册渠道，0:管理员，1:PC-Web，2:PC-H5，3:IOS-APP，4:Android-APP，5:微信小程序
     */
    private Integer registerChannel;

    /**
     * 用户来源，0:系统注册，1:外部导入(同步)
     */
    private Integer fromSource;

    /**
     * 说明
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;
}