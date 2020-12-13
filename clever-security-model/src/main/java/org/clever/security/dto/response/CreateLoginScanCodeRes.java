package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 21:16 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreateLoginScanCodeRes extends BaseResponse {
    /**
     * 扫描二维码
     */
    private String scanCode;

    /**
     * 扫描二维码状态，0:已创建(待扫描)，1:已扫描(待确认)，2:已确认(待登录)，3:登录成功，4:已失效
     */
    private Integer scanCodeState;

    /**
     * 扫描二维码过期时间(生成二维码 -> 扫码请求时间)
     */
    private Date expiredTime;

    /**
     * 扫描二维码内容(Base64编码处理)
     */
    private String scanCodeContent;
}
