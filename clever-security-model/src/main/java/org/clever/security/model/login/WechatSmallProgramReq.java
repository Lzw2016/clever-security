package org.clever.security.model.login;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 14:55 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WechatSmallProgramReq extends AbstractUserLoginReq {
    /**
     * 微信登录code
     */
    private String loginCode;
}
