package org.clever.security.model.login;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.security.LoginChannel;
import org.clever.security.LoginType;

import javax.validation.constraints.NotBlank;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 14:55 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WechatSmallProgramReq extends AbstractUserLoginReq {
    public static final String LoginCode_ParamName = "loginCode";

    /**
     * 微信登录code
     */
    @NotBlank(message = "微信登录code不能为空")
    private String loginCode;

    @Override
    public LoginType getLoginType() {
        return LoginType.WechatSmallProgram;
    }

    @Override
    public String getLoginChannel() {
        return LoginChannel.WechatSmallProgram.getName();
    }

    @Override
    public void setLoginChannel(String loginChannel) {
        throw new UnsupportedOperationException("微信小程序登录，不支持设置登录渠道");
    }
}
