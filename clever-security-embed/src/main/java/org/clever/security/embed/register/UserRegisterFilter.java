package org.clever.security.embed.register;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.RegisterSupportClient;
import org.clever.security.dto.response.UserRegisterRes;
import org.clever.security.embed.collect.RegisterDataCollect;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.UserRegisterConfig;
import org.clever.security.embed.event.RegisterFailureEvent;
import org.clever.security.embed.event.RegisterSuccessEvent;
import org.clever.security.embed.exception.CollectRegisterDataException;
import org.clever.security.embed.exception.RegisterException;
import org.clever.security.embed.exception.RegisterInnerException;
import org.clever.security.embed.handler.RegisterFailureHandler;
import org.clever.security.embed.handler.RegisterSuccessHandler;
import org.clever.security.embed.utils.HttpServletResponseUtils;
import org.clever.security.embed.utils.ListSortUtils;
import org.clever.security.embed.utils.PathFilterUtils;
import org.clever.security.model.register.AbstractUserRegisterReq;
import org.clever.security.model.register.EmailRegisterReq;
import org.clever.security.model.register.LoginNameRegisterReq;
import org.clever.security.model.register.SmsRegisterReq;
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
     * 校验注册数据(字段格式、验证码等等)
     */
    private final List<VerifyRegisterData> verifyRegisterDataList;
    /**
     * 注册成功处理
     */
    private final List<RegisterSuccessHandler> registerSuccessHandlerList;
    /**
     * 注册失败处理
     */
    private final List<RegisterFailureHandler> registerFailureHandlerList;

    private final RegisterSupportClient registerSupportClient;

    public UserRegisterFilter(
            SecurityConfig securityConfig,
            List<RegisterDataCollect> registerDataCollectList,
            List<VerifyRegisterData> verifyRegisterDataList,
            List<RegisterSuccessHandler> registerSuccessHandlerList,
            List<RegisterFailureHandler> registerFailureHandlerList,
            RegisterSupportClient registerSupportClient) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notEmpty(registerDataCollectList, "注册数据收集器(RegisterDataCollect)不存在");
        Assert.notEmpty(verifyRegisterDataList, "用户注册验证器(VerifyRegisterData)不存在");
        Assert.notNull(registerSupportClient, "参数registerSupportClient不能为空");
        this.securityConfig = securityConfig;
        this.registerDataCollectList = ListSortUtils.sort(registerDataCollectList);
        this.verifyRegisterDataList = ListSortUtils.sort(verifyRegisterDataList);
        this.registerSuccessHandlerList = ListSortUtils.sort(registerSuccessHandlerList);
        this.registerFailureHandlerList = ListSortUtils.sort(registerFailureHandlerList);
        this.registerSupportClient = registerSupportClient;
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
        if (!PathFilterUtils.isRegisterRequest(httpRequest, securityConfig) || !enableRegister()) {
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

    /**
     * 是否启用了注册功能
     */
    protected boolean enableRegister() {
        UserRegisterConfig register = securityConfig.getRegister();
        if (register == null) {
            return false;
        }
        return register.isEnableLoginNameRegister() || register.isEnableSmsRegister() || register.isEnableEmailRegister();
    }

    /**
     * 注册流程
     */
    protected void register(RegisterContext context) throws Exception {
        // 收集注册数据
        AbstractUserRegisterReq registerReq = null;
        for (RegisterDataCollect collect : registerDataCollectList) {
            if (!collect.isSupported(securityConfig, context.getRequest())) {
                continue;
            }
            try {
                registerReq = collect.collectRegisterData(securityConfig, context.getRequest());
            } catch (Exception e) {
                throw new CollectRegisterDataException("读取注册数据失败", e);
            }
            if (registerReq != null) {
                break;
            }
        }
        log.debug("### 收集注册数据 -> {}", registerReq);
        if (registerReq == null) {
            throw new RegisterInnerException("不支持的注册请求(无法获取注册数据)");
        }
        context.setRegisterData(registerReq);
        // 加载用户之前校验注册数据
        for (VerifyRegisterData verifyRegisterData : verifyRegisterDataList) {
            if (!verifyRegisterData.isSupported(securityConfig, context.getRequest(), registerReq)) {
                continue;
            }
            try {
                verifyRegisterData.verify(securityConfig, context.getRequest(), registerReq);
            } catch (RegisterException e) {
                context.setRegisterException(e);
                break;
            }
        }
        // 注册失败
        if (context.isRegisterFailure()) {
            log.debug("### 加载用户之前校验注册数据失败(注册失败) -> {}", registerReq);
            throw context.getRegisterException();
        }
        log.debug("### 加载用户之前校验注册数据成功 -> {}", registerReq);
        // 注册用户
        UserRegisterRes userRegisterRes;
        if (registerReq instanceof LoginNameRegisterReq) {
            userRegisterRes = registerSupportClient.registerByLoginName((LoginNameRegisterReq) registerReq);
        } else if (registerReq instanceof SmsRegisterReq) {
            userRegisterRes = registerSupportClient.registerBySms((SmsRegisterReq) registerReq);
        } else if (registerReq instanceof EmailRegisterReq) {
            userRegisterRes = registerSupportClient.registerByEmail((EmailRegisterReq) registerReq);
        } else {
            throw new RegisterInnerException(String.format("不支持的注册数据类型: %s", registerReq.getClass().getName()));
        }
        context.setUser(userRegisterRes);
        log.debug("### 用户注册成功 -> {}", userRegisterRes);
    }

    /**
     * 注册成功处理
     */
    protected void registerSuccessHandler(RegisterContext context) throws Exception {
        if (registerSuccessHandlerList == null) {
            return;
        }
        RegisterSuccessEvent registerSuccessEvent = new RegisterSuccessEvent();
        for (RegisterSuccessHandler handler : registerSuccessHandlerList) {
            handler.onRegisterSuccess(context.getRequest(), context.getResponse(), registerSuccessEvent);
        }
    }

    /**
     * 注册失败处理
     */
    protected void registerFailureHandler(RegisterContext context) throws Exception {
        if (registerFailureHandlerList == null) {
            return;
        }
        RegisterFailureEvent loginFailureEvent = new RegisterFailureEvent();
        for (RegisterFailureHandler handler : registerFailureHandlerList) {
            handler.onRegisterFailure(context.getRequest(), context.getResponse(), loginFailureEvent);
        }
    }

    /**
     * 当注册成功时响应处理
     */
    protected void onRegisterSuccessResponse(RegisterContext context) throws IOException {
        if (context.getResponse().isCommitted()) {
            return;
        }
    }

    /**
     * 当注册失败时响应处理
     */
    protected void onRegisterFailureResponse(RegisterContext context) throws IOException {
        if (context.getResponse().isCommitted()) {
            return;
        }
    }
}
