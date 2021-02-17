package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;
import org.clever.common.validation.StringNotBlank;
import org.clever.common.validation.ValidIntegerStatus;
import org.clever.security.entity.EnumConstant;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/17 14:07 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServerAccessTokenUpdateReq extends BaseRequest {
    /**
     * TokenID
     */
    @NotNull(message = "TokenID不能为null")
    private Long id;

    /**
     * Token标签
     */
    @StringNotBlank(message = "Token标签不能为空")
    private String tag;

    /**
     * Token名称
     */
    @StringNotBlank(message = "Token名称不能为空")
    private String tokenName;

    /**
     * Token值
     */
    @StringNotBlank(message = "Token值不能为空")
    private String tokenValue;

    /**
     * Token过期时间(空表示永不过期)
     */
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
