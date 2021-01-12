package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 21:58 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class ConfirmLoginScanCodeRes extends BaseResponse {
    /**
     * 用户id
     */
    private String uid;

    /**
     * 确认登录时间
     */
    private Date confirmTime;

    /**
     * 获取登录JWT-Token过期时间(确认登录 -> 获取登录Token时间)
     */
    private Date getTokenExpiredTime;
}
