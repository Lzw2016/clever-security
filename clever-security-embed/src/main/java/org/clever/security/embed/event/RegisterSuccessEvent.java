package org.clever.security.embed.event;

import lombok.Data;
import org.clever.security.dto.response.UserRegisterRes;

import java.io.Serializable;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 16:13 <br/>
 */
@Data
public class RegisterSuccessEvent implements Serializable {
    /**
     * 域id
     */
    private final Long domainId;
    /**
     * 注册渠道，0:PC-Admin，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序
     */
    private final Integer registerChannel;
    /**
     * 注册类型，1:登录名注册，2:手机号注册，3:邮箱注册，4:微信小程序注册，
     */
    private final Integer registerType;
    /**
     * 注册的用户信息
     */
    private final UserRegisterRes userRegisterRes;

    public RegisterSuccessEvent(Long domainId, Integer registerChannel, Integer registerType, UserRegisterRes userRegisterRes) {
        this.domainId = domainId;
        this.registerChannel = registerChannel;
        this.registerType = registerType;
        this.userRegisterRes = userRegisterRes;
    }
}
