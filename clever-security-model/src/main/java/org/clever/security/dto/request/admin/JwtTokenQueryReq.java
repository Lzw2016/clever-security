package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/17 16:40 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JwtTokenQueryReq extends QueryByPage {
    /**
     * JWT-Token ID
     */
    private Long id;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * uid、login_name、telephone、email、nickname
     */
    private String userSearchKey;

    /**
     * JWT-Token过期时间(空表示永不过期) - 开始
     */
    private Date expiredTimeStart;

    /**
     * JWT-Token过期时间(空表示永不过期) - 结束
     */
    private Date expiredTimeEnd;

    /**
     * JWT-Token是否禁用，0:未禁用, 1:已禁用
     */
    private Integer disable;

    /**
     * 刷新Token
     */
    private String refreshToken;

    /**
     * 刷新Token状态，0:无效(已使用), 1:有效(未使用)
     */
    private Integer refreshTokenState;

    /**
     * 创建时间
     */
    private Date createAtStart;

    /**
     * 创建时间
     */
    private Date createAtEnd;
}
