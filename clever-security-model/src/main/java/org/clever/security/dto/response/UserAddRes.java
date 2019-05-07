package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 20:57 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAddRes extends BaseResponse {
    /** 主键id */
    private Long id;

    /** 登录名 */
    private String username;

    /** 用户类型，0：系统内建，1：外部系统用户 */
    private Integer userType;

    /** 手机号 */
    private String telephone;

    /** 邮箱 */
    private String email;

    /** 帐号过期时间 */
    private Date expiredTime;

    /** 帐号是否锁定，0：未锁定；1：锁定 */
    private Integer locked;

    /** 是否启用，0：禁用；1：启用 */
    private Integer enabled;

    /** 说明 */
    private String description;

    /** 创建时间 */
    private Date createAt;

    /** 更新时间 */
    private Date updateAt;
}
