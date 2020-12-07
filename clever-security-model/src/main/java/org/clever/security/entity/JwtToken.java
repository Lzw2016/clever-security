package org.clever.security.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * JWT-Token表(JwtToken)实体类
 *
 * @author lizw
 * @since 2020-11-28 19:47:39
 */
@Data
public class JwtToken implements Serializable {
    private static final long serialVersionUID = -98330312308317787L;
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
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;
}