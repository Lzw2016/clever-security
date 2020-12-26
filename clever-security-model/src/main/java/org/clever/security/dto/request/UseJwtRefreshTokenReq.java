package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 作者：yz <br/>
 * 创建时间：2020-12-24 21:28 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UseJwtRefreshTokenReq extends BaseRequest {
    @NotNull(message = "域id不能为空")
    private Long domainId;
    /**
     * 过期的JWT-Token id
     */
    @NotNull(message = "过期的JWT-Token id不能为null")
    private Long useJwtId;
    /**
     * 使用的刷新Token
     */
    @NotBlank(message = "使用的刷新Token不能为空")
    private String useRefreshToken;

    /**
     * JWT-Token id(系统自动生成且不会变化)
     */
    @NotNull(message = "JWT-Token id不能为null")
    private Long jwtId;
    /**
     * token数据
     */
    @NotBlank(message = "token数据不能为空")
    private String token;
    /**
     * JWT-Token过期时间(空表示永不过期)
     */
    @NotNull(message = "JWT-Token过期时间不能为null")
    private Date expiredTime;
    /**
     * 刷新Token
     */
    @NotBlank(message = "刷新Token不能为空")
    private String refreshToken;
    /**
     * 刷新Token过期时间
     */
    @NotNull(message = "刷新Token过期时间不能为空")
    private Date refreshTokenExpiredTime;

    public UseJwtRefreshTokenReq(Long domainId) {
        this.domainId = domainId;
    }
}
