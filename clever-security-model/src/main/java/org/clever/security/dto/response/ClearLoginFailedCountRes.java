package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/14 20:59 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ClearLoginFailedCountRes extends BaseResponse {
    /**
     * 登录用户id
     */
    private String uid;

    /**
     * 登录方式，1:用户名密码，2:手机号验证码，3:邮箱验证码，4:刷新令牌，5:微信小程序，6:扫码登录
     */
    private Integer loginType;

    /**
     * 登录失败次数
     */
    private Integer failedCount;

    /**
     * 最后登录失败时间
     */
    private Date lastLoginTime;
}
