package org.clever.security.embed.extend;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 密码找回过滤器
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020-12-16 22:27 <br/>
 */
public class PasswordRecoveryFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // TODO 密码找回
    }
}
