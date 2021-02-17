package org.clever.security.dto.response.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/17 16:41 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JwtTokenQueryRes extends BaseResponse {
    /**
     * JWT-Token id(系统自动生成且不会变化)
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
     * token数据
     */
    private String token;

    /**
     * JWT-Token过期时间(空表示永不过期)
     */
    private Date expiredTime;

    /**
     * JWT-Token是否禁用，0:未禁用, 1:已禁用
     */
    private Integer disable;

    /**
     * JWT-Token禁用原因
     */
    private String disableReason;

    /**
     * 刷新Token
     */
    private String refreshToken;

    /**
     * 刷新Token过期时间
     */
    private Date refreshTokenExpiredTime;

    /**
     * 刷新Token状态，0:无效(已使用), 1:有效(未使用)
     */
    private Integer refreshTokenState;

    /**
     * 刷新Token使用时间
     */
    private Date refreshTokenUseTime;

    /**
     * 刷新token创建的JWT-Token id
     */
    private Long refreshCreateTokenId;

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
