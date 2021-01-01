package org.clever.security.jackson2;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.clever.security.model.SecurityContext;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/19 13:44 <br/>
 */
public class CleverSecurityJackson2Module extends SimpleModule {
    public static final CleverSecurityJackson2Module instance = new CleverSecurityJackson2Module();

    public CleverSecurityJackson2Module() {
        super(CleverSecurityJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(SecurityContext.class, SecurityContextMixin.class);
    }
}
