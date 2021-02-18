package org.clever.security.dto.response.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/18 15:29 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserSecurityContextQueryRes extends BaseResponse {
    /**
     * id(系统自动生成且不会变化)
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
     * 用户安全信息(安全上下文)
     */
    private String securityContext;

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

    // --------------------------------------------------------------------------------------------------------- user

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
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;
}
