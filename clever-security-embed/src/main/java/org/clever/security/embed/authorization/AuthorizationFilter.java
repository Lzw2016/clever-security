package org.clever.security.embed.authorization;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.authorization.voter.AuthorizationVoter;
import org.clever.security.embed.authorization.voter.VoterResult;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.context.SecurityContextHolder;
import org.clever.security.embed.event.AuthorizationFailureEvent;
import org.clever.security.embed.event.AuthorizationSuccessEvent;
import org.clever.security.embed.exception.AuthorizationInnerException;
import org.clever.security.embed.handler.AuthorizationFailureHandler;
import org.clever.security.embed.handler.AuthorizationSuccessHandler;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.ListSortUtils;
import org.clever.security.embed.utils.PathFilterUtils;
import org.clever.security.model.SecurityContext;
import org.clever.security.model.UserInfo;
import org.clever.security.model.auth.ForbiddenAccessRes;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

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
     * 授权成功的处理
     */
    private final List<AuthorizationSuccessHandler> authorizationSuccessHandlerList;
    /**
     * 授权失败的处理
     */
    private final List<AuthorizationFailureHandler> authorizationFailureHandlerList;

    public AuthorizationFilter(
            SecurityConfig securityConfig,
            List<AuthorizationVoter> authorizationVoterList,
            List<AuthorizationSuccessHandler> authorizationSuccessHandlerList,
            List<AuthorizationFailureHandler> authorizationFailureHandlerList) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notNull(authorizationVoterList, "授权投票器(AuthorizationVoter)不能为null");
        Assert.notNull(authorizationSuccessHandlerList, "授权成功的处理(AuthorizationSuccessHandler)不能为null");
        Assert.notNull(authorizationFailureHandlerList, "授权成功的处理(AuthorizationFailureHandler)不能为null");
        this.securityConfig = securityConfig;
        this.authorizationVoterList = ListSortUtils.sort(authorizationVoterList);
        this.authorizationSuccessHandlerList = ListSortUtils.sort(authorizationSuccessHandlerList);
        this.authorizationFailureHandlerList = ListSortUtils.sort(authorizationFailureHandlerList);
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
        boolean pass;
        try {
            pass = authorization(context);
            log.debug("### 授权完成，结果: [{}]", pass ? "通过" : "拒绝");
        } catch (Throwable e) {
            // 授权异常
            log.error("授权异常", e);
            HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, e);
            return;
        } finally {
            log.debug("### 授权逻辑行完成 <----------------------------------------------------------------------");
        }
        // 执行授权事件
        try {
            if (pass) {
                onAuthorizationSuccess(context);
            } else {
                onAuthorizationFailure(context);
                // 无权访问 403
                onAuthorizationFailureResponse(context);
            }
        } catch (Throwable e) {
            log.error("授权异常", e);
            HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
        // 处理业务逻辑
        chain.doFilter(request, response);
    }

    protected boolean authorization(AuthorizationContext context) {
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
                log.debug("### 授权通过 | 权重={} | Voter={}", authorizationVoter.getWeight(), authorizationVoter.getClass().getName());
                passWeight = passWeight + authorizationVoter.getWeight();
            } else if (Objects.equals(VoterResult.REJECT.getId(), voterResult.getId())) {
                // 驳回
                log.debug("### 授权驳回 | 权重={} | Voter={}", authorizationVoter.getWeight(), authorizationVoter.getClass().getName());
                passWeight = passWeight - authorizationVoter.getWeight();
            } else if (Objects.equals(VoterResult.ABSTAIN.getId(), voterResult.getId())) {
                // 弃权
                log.debug("### 放弃授权 | 权重={} | Voter={}", authorizationVoter.getWeight(), authorizationVoter.getClass().getName());
            } else {
                throw new AuthorizationInnerException("未知的授权投票结果");
            }
        }
        return passWeight >= 0;
    }

    /**
     * 当授权成功时处理
     */
    protected void onAuthorizationSuccess(AuthorizationContext context) {
        SecurityContext securityContext = context.getSecurityContext();
        AuthorizationSuccessEvent event = new AuthorizationSuccessEvent(securityContext.getUserInfo(), securityContext.getRoles(), securityContext.getRoles());
        for (AuthorizationSuccessHandler handler : authorizationSuccessHandlerList) {
            handler.onAuthorizationSuccess(context.getRequest(), context.getResponse(), event);
        }
    }

    /**
     * 当授权失败时处理
     */
    protected void onAuthorizationFailure(AuthorizationContext context) {
        SecurityContext securityContext = context.getSecurityContext();
        AuthorizationFailureEvent event = new AuthorizationFailureEvent(securityContext.getUserInfo(), securityContext.getRoles(), securityContext.getRoles());
        for (AuthorizationFailureHandler handler : authorizationFailureHandlerList) {
            handler.onAuthorizationFailure(context.getRequest(), context.getResponse(), event);
        }
    }

    /**
     * 当授权失败时响应处理
     */
    protected void onAuthorizationFailureResponse(AuthorizationContext context) throws IOException {
        HttpServletResponse response = context.getResponse();
        if (response.isCommitted()) {
            return;
        }
        if (securityConfig.isForbiddenNeedRedirect()) {
            // 需要重定向
            HttpServletResponseUtils.redirect(response, securityConfig.getNotLoginRedirectPage());
        } else {
            // 直接返回
            SecurityContext securityContext = context.getSecurityContext();
            UserInfo userInfo = securityContext == null ? null : securityContext.getUserInfo();
            ForbiddenAccessRes forbiddenAccessRes = new ForbiddenAccessRes(userInfo, "未授权，禁止访问");
            HttpServletResponseUtils.sendJson(response, forbiddenAccessRes, HttpStatus.FORBIDDEN);
        }
    }
}
