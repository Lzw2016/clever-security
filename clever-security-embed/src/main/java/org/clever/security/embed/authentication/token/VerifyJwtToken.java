package org.clever.security.embed.authentication.token;

import io.jsonwebtoken.Claims;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.AuthenticationException;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT-Token验证器
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/12/05 12:40 <br/>
 */
public interface VerifyJwtToken extends Ordered {
    /**
     * 验证JWT-Token
     *
     * @param jwtToken       JWT-Token
     * @param uid            用户id
     * @param claims         JWT-Token Body内容
     * @param securityConfig 权限系统配置
     * @param request        请求对象
     * @param response       响应对象
     */
    void verify(String jwtToken, String uid, Claims claims, SecurityConfig securityConfig, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException;
}
