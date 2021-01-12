package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 17:25 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class GetScanCodeLoginInfoRes extends BaseResponse {
    /**
     * 扫描二维码
     */
    private String scanCode;
    /**
     * 扫描二维码状态，0:已创建(待扫描)，1:已扫描(待确认)，2:已确认(待登录)，3:登录成功，4:已失效
     */
    private Integer scanCodeState;
    /**
     * 绑定的JWT-Token id
     */
    private Long bindTokenId;
    /**
     * 获取登录JWT-Token过期时间(确认登录 -> 获取登录Token时间)
     */
    private Date getTokenExpiredTime;
}
