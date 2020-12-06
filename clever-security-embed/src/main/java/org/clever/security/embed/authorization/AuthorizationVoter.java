package org.clever.security.embed.authorization;

import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.model.SecurityContext;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 授权投票器
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/12/06 18:08 <br/>
 */
public interface AuthorizationVoter extends Ordered {
    double Default_Weight = 1.0;

    /**
     * 返回当前投票器权重
     */
    default double getWeight() {
        return Default_Weight;
    }

    /**
     * 投票当前用户是否能访问资源
     *
     * @param securityConfig  权限系统配置
     * @param request         请求对象
     * @param securityContext 安全上下文(用户信息)
     * @return 投票结果
     */
    VoterResult vote(SecurityConfig securityConfig, HttpServletRequest request, SecurityContext securityContext);
}
