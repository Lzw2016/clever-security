package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-11 21:22 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class AddUserRegisterLogRes extends BaseResponse {
    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 注册IP
     */
    private String registerIp;

    /**
     * 注册渠道，0:PC-Admin，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序
     */
    private Integer registerChannel;

    /**
     * 注册类型，1:登录名注册，2:手机号注册，3:邮箱注册，4:微信小程序注册，
     */
    private Integer registerType;

    /**
     * 注册结果，0:注册失败，1:注册成功且创建用户，2:注册成功仅关联到域
     */
    private Integer requestResult;

    /**
     * 注册成功的用户id
     */
    private String registerUid;
}
