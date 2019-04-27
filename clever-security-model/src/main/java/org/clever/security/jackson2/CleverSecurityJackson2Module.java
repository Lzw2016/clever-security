package org.clever.security.jackson2;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.clever.security.model.LoginUserDetails;
import org.clever.security.model.UserAuthority;
import org.clever.security.token.UserLoginToken;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-22 21:21 <br/>
 */
public class CleverSecurityJackson2Module extends SimpleModule {

    public CleverSecurityJackson2Module() {
        super(CleverSecurityJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(UserLoginToken.class, UserLoginTokenMixin.class);
        context.setMixInAnnotations(LoginUserDetails.class, LoginUserDetailsMixin.class);
        context.setMixInAnnotations(UserAuthority.class, UserAuthorityMixin.class);
    }
}
