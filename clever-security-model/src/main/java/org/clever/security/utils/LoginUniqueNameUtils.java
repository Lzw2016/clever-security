package org.clever.security.utils;

import org.clever.security.model.login.AbstractUserLoginReq;
import org.clever.security.model.login.EmailValidateCodeReq;
import org.clever.security.model.login.LoginNamePasswordReq;
import org.clever.security.model.login.SmsValidateCodeReq;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/29 19:39 <br/>
 */
public class LoginUniqueNameUtils {
    /**
     * 获取用户登录唯一名
     */
    public static String getLoginUniqueName(AbstractUserLoginReq loginReq) {
        String loginUniqueName = null;
        if (loginReq instanceof LoginNamePasswordReq) {
            loginUniqueName = ((LoginNamePasswordReq) loginReq).getLoginName();
        } else if (loginReq instanceof SmsValidateCodeReq) {
            loginUniqueName = ((SmsValidateCodeReq) loginReq).getTelephone();
        } else if (loginReq instanceof EmailValidateCodeReq) {
            loginUniqueName = ((EmailValidateCodeReq) loginReq).getEmail();
        }
        return loginUniqueName;
    }
}
