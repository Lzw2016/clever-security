package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/14 20:22 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class AddJwtTokenReq extends BaseRequest {
    /**
     * JWT-Token id(系统自动生成且不会变化)
     */
    @NotNull(message = "JWT-Token id不能为null")
    private Long id;
    /**
     * 域id
     */
    @NotNull(message = "不能为null")
    private Long domainId;

    /**
     * 用户id
     */
    @NotBlank(message = "用户id不能为空")
    private String uid;

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
    private String refreshToken;

    /**
     * 刷新Token过期时间
     */
    private Date refreshTokenExpiredTime;

    public AddJwtTokenReq(Long domainId) {
        this.domainId = domainId;
    }
}
