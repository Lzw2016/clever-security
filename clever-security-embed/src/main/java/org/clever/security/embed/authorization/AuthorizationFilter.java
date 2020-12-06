package org.clever.security.embed.authorization;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.context.SecurityContextHolder;
import org.clever.security.embed.exception.AuthorizationException;
import org.clever.security.embed.exception.AuthorizationInnerException;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.ListSortUtils;
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
import java.util.List;
import java.util.Objects;

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
     * 授权投票器
     */
    private final List<AuthorizationVoter> authorizationVoterList;
    /**
     *
     */
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public AuthorizationFilter(
            SecurityConfig securityConfig,
            List<AuthorizationVoter> authorizationVoterList,
            RequestMappingHandlerMapping requestMappingHandlerMapping) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notNull(authorizationVoterList, "授权投票器(AuthorizationVoter)不能为null");
        Assert.notNull(requestMappingHandlerMapping, "参数requestMappingHandlerMapping不能为null");
        this.securityConfig = securityConfig;
        this.authorizationVoterList = ListSortUtils.sort(authorizationVoterList);
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
        AuthorizationContext context = new AuthorizationContext(httpRequest, httpResponse);
        boolean pass = false;
        try {
            pass = authorization(context);
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
        if (!pass) {
            // TODO 无权访问 403
            return;
        }
        chain.doFilter(request, response);
    }

    protected boolean authorization(AuthorizationContext context) throws Exception {
        SecurityContext securityContext = SecurityContextHolder.getContext(context.getRequest());
        if (securityContext == null) {
            throw new AuthorizationInnerException("获取SecurityContext失败");
        }
        context.setSecurityContext(securityContext);
        // 开始授权
        double passWeight = 0;
        for (AuthorizationVoter authorizationVoter : authorizationVoterList) {
            VoterResult voterResult = authorizationVoter.vote(securityConfig, context.getRequest(), securityContext);
            if (voterResult == null) {
                context.setAuthorizationException(new AuthorizationInnerException("授权投票结果为null"));
                throw context.getAuthorizationException();
            } else if (Objects.equals(VoterResult.PASS.getId(), voterResult.getId())) {
                // 通过
                log.debug("### 授权通过");
                passWeight = passWeight + authorizationVoter.getWeight();
            } else if (Objects.equals(VoterResult.REJECT.getId(), voterResult.getId())) {
                // 驳回
                log.debug("### 授权驳回");
                passWeight = passWeight - authorizationVoter.getWeight();
            } else if (Objects.equals(VoterResult.ABSTAIN.getId(), voterResult.getId())) {
                // 弃权
                log.debug("### 放弃授权");
            } else {
                throw new AuthorizationInnerException("未知的授权投票结果");
            }
        }

        HandlerExecutionChain handlerExecutionChain = requestMappingHandlerMapping.getHandler(context.getRequest());
        if (handlerExecutionChain == null) {
            // TODO
            throw new AuthorizationInnerException("获取HandlerExecutionChain失败");
        }
        HandlerMethod handlerMethod = null;
        Object handler = handlerExecutionChain.getHandler();
        if (handler instanceof HandlerMethod) {
            handlerMethod = (HandlerMethod) handler;
        }
        if (handlerMethod == null) {
            throw new AuthorizationInnerException("不支持的HandlerMethod");
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


        return passWeight >= 0;
    }
}
