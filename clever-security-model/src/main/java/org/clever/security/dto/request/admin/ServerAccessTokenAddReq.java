package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.clever.common.validation.ValidIntegerStatus;
import org.clever.security.entity.EnumConstant;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/17 13:00 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServerAccessTokenAddReq extends BaseRequest {
    /**
     * 域id
     */
    @NotNull(message = "域ID不能为null")
    private Long domainId;

    /**
     * Token标签
     */
    private String tag;

    /**
     * Token名称
     */
    @NotBlank(message = "Token名称不能为空")
    private String tokenName;

    /**
     * Token值
     */
    @NotBlank(message = "Token值不能为空")
    private String tokenValue;

    /**
     * Token过期时间(空表示永不过期)
     */
    @Future
    private Date expiredTime;

    /**
     * Token是否禁用，0:未禁用；1:已禁用
     */
    @ValidIntegerStatus(
            value = {EnumConstant.ServerAccessToken_Disable_0, EnumConstant.ServerAccessToken_Disable_1},
            message = "Token是否禁用，0:未禁用；1:已禁用"
    )
    private Integer disable;

    /**
     * 说明
     */
    private String description;
}
