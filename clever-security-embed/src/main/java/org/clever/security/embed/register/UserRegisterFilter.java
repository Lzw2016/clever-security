package org.clever.security.embed.register;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.collect.RegisterDataCollect;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.RegisterException;
import org.clever.security.embed.handler.RegisterFailureHandler;
import org.clever.security.embed.handler.RegisterSuccessHandler;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.ListSortUtils;
import org.clever.security.embed.utils.PathFilterUtils;
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

/**
 * 用户注册过滤器
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020-12-16 22:25 <br/>
 */
@Slf4j
public class UserRegisterFilter extends GenericFilterBean {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    /**
     * 收集注册数据
     */
    private final List<RegisterDataCollect> registerDataCollectList;
    /**
     * 注册成功处理
     */
    private final List<RegisterSuccessHandler> registerSuccessHandlerList;
    /**
     * 注册失败处理
     */
    private final List<RegisterFailureHandler> registerFailureHandlerList;
    
    public UserRegisterFilter(
            SecurityConfig securityConfig,
            List<RegisterDataCollect> registerDataCollectList,
            List<RegisterSuccessHandler> registerSuccessHandlerList,
            List<RegisterFailureHandler> registerFailureHandlerList) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notEmpty(registerDataCollectList, "注册数据收集器(RegisterDataCollect)不存在");
        this.securityConfig = securityConfig;
        this.registerDataCollectList = ListSortUtils.sort(registerDataCollectList);
        this.registerSuccessHandlerList = ListSortUtils.sort(registerSuccessHandlerList);
        this.registerFailureHandlerList = ListSortUtils.sort(registerFailureHandlerList);
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
        if (!PathFilterUtils.isRegisterRequest(httpRequest, securityConfig)) {
            // 不是注册请求
            chain.doFilter(request, response);
            return;
        }
        log.debug("### 开始执行注册逻辑 ---------------------------------------------------------------------->");
        log.debug("当前请求 -> [{}]", httpRequest.getRequestURI());
        // 执行注册逻辑
        RegisterContext context = new RegisterContext(httpRequest, httpResponse);
        try {
            register(context);
            // 注册成功处理
            registerSuccessHandler(context);
            // 注册成功 - 返回数据给客户端
            onRegisterSuccessResponse(context);
        } catch (RegisterException e) {
            // 注册失败
            log.debug("### 注册失败", e);
            if (context.getRegisterException() == null) {
                context.setRegisterException(e);
            }
            try {
                // 注册失败处理
                registerFailureHandler(context);
                // 返回数据给客户端
                onRegisterFailureResponse(context);
            } catch (Exception innerException) {
                log.error("注册异常", innerException);
                HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, innerException);
            }
        } catch (Throwable e) {
            // 注册异常
            log.error("注册异常", e);
            HttpServletResponseUtils.sendJson(httpRequest, httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, e);
        } finally {
            log.debug("### 注册逻辑执行完成 <----------------------------------------------------------------------");
        }
    }

    protected void register(RegisterContext context) throws Exception {

    }

    /**
     * 注册成功处理
     */
    protected void registerSuccessHandler(RegisterContext context) throws Exception {

    }

    /**
     * 注册失败处理
     */
    protected void registerFailureHandler(RegisterContext context) throws Exception {

    }

    /**
     * 当注册成功时响应处理
     */
    protected void onRegisterSuccessResponse(RegisterContext context) throws IOException {

    }

    /**
     * 当注册失败时响应处理
     */
    protected void onRegisterFailureResponse(RegisterContext context) throws IOException {

    }
}
