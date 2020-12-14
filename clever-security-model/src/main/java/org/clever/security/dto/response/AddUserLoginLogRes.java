package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/14 19:36 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddUserLoginLogRes extends BaseResponse {
    /**
     * 登录日志id(系统自动生成且不会变化)
     */
    private Long id;

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
     * JWT-Token id
     */
    private Long jwtTokenId;
}
