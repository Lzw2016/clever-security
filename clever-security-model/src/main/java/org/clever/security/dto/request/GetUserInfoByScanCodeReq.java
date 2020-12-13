package org.clever.security.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 20:30 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetUserInfoByScanCodeReq extends BaseRequest {
    /**
     * 浏览器扫描码
     */
    @NotBlank(message = "二维码不能为空")
    private String scanCode;
}
