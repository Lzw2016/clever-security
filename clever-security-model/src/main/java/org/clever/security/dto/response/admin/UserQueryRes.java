package org.clever.security.dto.response.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/03/09 21:17 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRes extends BaseResponse {
    /**
     * 用户id(系统自动生成且不会变化)
     */
    private String uid;

    /**
     * 用户登录名(允许修改)
     */
    private String loginName;

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
     * 用户注册渠道，0:管理员，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序
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

    // --------------------------------------------------------------------------------------------------------- domain

    /**
     * 域名称
     */
    private String domainName;
}
