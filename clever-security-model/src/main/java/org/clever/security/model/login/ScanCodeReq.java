package org.clever.security.model.login;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 14:45 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ScanCodeReq extends AbstractUserLoginReq {
    /**
     * 浏览器扫描码
     */
    @NotBlank(message = "二维码不能为空")
    private String browseScanCode;
    /***
     * 用户登录的Token
     */
    @NotBlank(message = "Token不能为空")
    private String loginToken;
}
