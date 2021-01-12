package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 21:40 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class BindLoginScanCodeRes extends BaseResponse {
    /**
     * 用户id
     */
    private String uid;

    /**
     * (扫描时间)绑定JWT-Token时间
     */
    private Date bindTokenTime;

    /**
     * 确认登录过期时间(扫码二维码 -> 确认登录时间)
     */
    private Date confirmExpiredTime;
}
