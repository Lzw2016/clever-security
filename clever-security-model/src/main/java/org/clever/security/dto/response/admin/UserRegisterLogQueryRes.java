package org.clever.security.dto.response.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

import java.util.Date;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:55 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRegisterLogQueryRes extends QueryByPage {
    /**
     * id
     */
    private Long id;

    /**
     * 注册的域id
     */
    private Long registerDomainId;

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
     * 注册请求数据
     */
    private String requestData;

    /**
     * 注册结果，0:注册失败，1:注册成功且创建用户，2:注册成功仅关联到域
     */
    private Integer requestResult;

    /**
     * 注册成功的用户id
     */
    private String registerUid;

    /**
     * 注册失败原因
     */
    private String failReason;

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