package org.clever.security.embed.authentication.login;

import org.clever.security.embed.config.internal.TokenConfig;
import org.clever.security.model.UserInfo;
import org.springframework.core.Ordered;

import java.util.Map;

/**
 * 创建JWT-Token是加入扩展数据
 * 作者：lizw <br/>
 * 创建时间：2020/12/02 22:54 <br/>
 */
public interface AddJwtTokenExtData extends Ordered {
    /**
     * 向JWT-Token中加入自定义扩展数据
     *
     * @param tokenConfig Token配置
     * @param userInfo    用户信息
     * @param extData     扩展数据
     * @return 扩展数据
     */
    Map<String, Object> addExtData(TokenConfig tokenConfig, UserInfo userInfo, Map<String, Object> extData);
}
