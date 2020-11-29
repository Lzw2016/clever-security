package org.clever.security.model.login;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.security.LoginChannel;
import org.clever.security.LoginType;

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

    @Override
    public LoginType getLoginType() {
        return LoginType.WechatSmallProgram;
    }

    @Override
    public LoginChannel getLoginChannel() {
        return LoginChannel.WechatSmallProgram;
    }

    @Override
    public void setLoginChannel(LoginChannel loginChannel) {
        throw new UnsupportedOperationException("微信小程序登录，不支持设置登录渠道");
    }
}
