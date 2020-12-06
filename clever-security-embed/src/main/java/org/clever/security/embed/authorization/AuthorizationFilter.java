package org.clever.security.embed.authorization;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.context.SecurityContextHolder;
import org.clever.security.embed.exception.AuthorizationException;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.PathFilterUtils;
import org.clever.security.model.SecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户权限认证拦截器(授权拦截器)
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 16:18 <br/>
 */
@Slf4j
public class AuthorizationFilter extends GenericFilterBean {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    /**
     *
     */
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public AuthorizationFilter(SecurityConfig securityConfig, RequestMappingHandlerMapping requestMappingHandlerMapping) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notNull(requestMappingHandlerMapping, "参数requestMappingHandlerMapping不能为null");
        this.securityConfig = securityConfig;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            log.warn("[clever-security]仅支持HTTP服务器");
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (!PathFilterUtils.isAuthorizationRequest(httpRequest, securityConfig)) {
            // 不需要授权
            chain.doFilter(request, response);
            return;
        }
        log.debug("### 开始执行授权逻辑 ---------------------------------------------------------------------->");
        // 执行授权逻辑
        try {
            authorization(httpRequest, httpResponse);
        } catch (AuthorizationException e) {
            // 授权失败
            log.debug("### 授权失败", e);
            // TODO 授权异常
        } catch (Throwable e) {
            // 授权异常
            log.error("授权异常", e);
            HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, e);
            return;
        } finally {
            log.debug("### 授权逻辑行完成 <----------------------------------------------------------------------");
        }
        // 处理业务逻辑
        chain.doFilter(request, response);
    }

    protected void authorization(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SecurityContext securityContext = SecurityContextHolder.getContext(request);
        if (securityContext == null) {
//            TODO throw new AuthorizationException
        }
        HandlerExecutionChain handlerExecutionChain = requestMappingHandlerMapping.getHandler(request);
        if (handlerExecutionChain == null) {
            // TODO
        }
        HandlerMethod handlerMethod = null;
        Object handler = handlerExecutionChain.getHandler();
        if (handler instanceof HandlerMethod) {
            handlerMethod = (HandlerMethod) handler;
        }
        if (handlerMethod == null) {
            // TODO
        }
        String targetClass = handlerMethod.getBeanType().getName();
        String targetMethod = handlerMethod.getMethod().getName();
        // 获取放签名
        StringBuilder methodParams = new StringBuilder();
        Class<?>[] paramTypes = handlerMethod.getMethod().getParameterTypes();
        for (Class<?> clazz : paramTypes) {
            if (methodParams.length() > 0) {
                methodParams.append(", ");
            }
            methodParams.append(clazz.getName());
        }
        // TODO authorization
    }
}
