package org.clever.security.dto.response.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/17 12:53 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserLoginLogQueryRes extends BaseResponse {
    /**
     * 登录日志id(系统自动生成且不会变化)
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
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 域名称
     */
    private String domainName;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户登录名
     */
    private String loginName;

    /**
     * 用户手机号
     */
    private String telephone;

    /**
     * 用户邮箱
     */
    private String email;
}
