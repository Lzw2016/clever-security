package org.clever.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户登录日志(UserLoginLog)实体类
 *
 * @author lizw
 * @since 2020-11-28 19:47:42
 */
@Data
public class UserLoginLog implements Serializable {
    private static final long serialVersionUID = 515728531537980535L;
    /**
     * 登录日志id(系统自动生成且不会变化)
     */
    @TableId(type = IdType.INPUT)
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
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录IP
     */
    private String loginIp;

    /**
     * 登录渠道，0:PC-Admin，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序
     */
    private Integer loginChannel;

    /**
     * 登录方式，1:用户名密码，2:手机号验证码，3:邮箱验证码，4:刷新token，5:微信小程序，6:扫码登录
     */
    private Integer loginType;

    /**
     * 登录状态，0:登录失败，1:登录成功
     */
    private Integer loginState;

    /**
     * 登录请求数据
     */
    private String requestData;

    /**
     * JWT-Token id
     */
    private Long jwtTokenId;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;
}