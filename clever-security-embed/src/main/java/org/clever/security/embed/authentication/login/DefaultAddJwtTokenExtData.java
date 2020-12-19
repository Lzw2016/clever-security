package org.clever.security.embed.authentication.login;

import org.clever.security.embed.config.internal.TokenConfig;
import org.clever.security.model.UserInfo;
import org.springframework.core.Ordered;

import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/19 22:50 <br/>
 */
public class DefaultAddJwtTokenExtData implements AddJwtTokenExtData {
    @Override
    public Map<String, Object> addExtData(TokenConfig tokenConfig, UserInfo userInfo, Map<String, Object> extData) {
        return extData;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
