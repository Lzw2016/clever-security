package org.clever.security.embed.extend;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/18 22:14 <br/>
 */
public class BindEmailFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // TODO 邮箱绑定/换绑
        chain.doFilter(request, response);
    }
}
