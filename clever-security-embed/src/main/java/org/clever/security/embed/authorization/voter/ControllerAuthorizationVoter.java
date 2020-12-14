package org.clever.security.embed.authorization.voter;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.AuthSupportClient;
import org.clever.security.dto.request.GetApiPermissionReq;
import org.clever.security.dto.response.GetApiPermissionRes;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.AuthorizationInnerException;
import org.clever.security.entity.EnumConstant;
import org.clever.security.model.SecurityContext;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Spring MVC Controller访问权限投票
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/12/06 20:27 <br/>
 */
@Slf4j
public class ControllerAuthorizationVoter implements AuthorizationVoter {
    private final AuthSupportClient authSupportClient;
    /**
     * Spring内置的根据request获取Controller Method的工具对象
     */
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public ControllerAuthorizationVoter(AuthSupportClient authSupportClient, RequestMappingHandlerMapping requestMappingHandlerMapping) {
        Assert.notNull(authSupportClient, "参数authSupportClient不能为null");
        Assert.notNull(requestMappingHandlerMapping, "参数requestMappingHandlerMapping不能为null");
        this.authSupportClient = authSupportClient;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public VoterResult vote(SecurityConfig securityConfig, HttpServletRequest request, SecurityContext securityContext) {
        HandlerExecutionChain handlerExecutionChain;
        try {
            handlerExecutionChain = requestMappingHandlerMapping.getHandler(request);
        } catch (Exception e) {
            throw new AuthorizationInnerException("获取HandlerExecutionChain异常");
        }
        if (handlerExecutionChain == null) {
            // 弃权
            return VoterResult.ABSTAIN;
        }
        HandlerMethod handlerMethod = null;
        Object handler = handlerExecutionChain.getHandler();
        if (handler instanceof HandlerMethod) {
            handlerMethod = (HandlerMethod) handler;
        }
        if (handlerMethod == null) {
            // 弃权
            return VoterResult.ABSTAIN;
        }
        String targetClass = handlerMethod.getBeanType().getName();
        String targetMethod = handlerMethod.getMethod().getName();
        StringBuilder methodParams = new StringBuilder();
        Class<?>[] paramTypes = handlerMethod.getMethod().getParameterTypes();
        for (Class<?> clazz : paramTypes) {
            if (methodParams.length() > 0) {
                methodParams.append(", ");
            }
            methodParams.append(clazz.getName());
        }
        // 根据targetClass、targetMethod、methodParams得到权限元数据
        GetApiPermissionReq req = new GetApiPermissionReq(securityConfig.getDomainId());
        req.setClassName(targetClass);
        req.setMethodName(targetMethod);
        req.setMethodParams(methodParams.toString());
        GetApiPermissionRes res = authSupportClient.getApiPermission(req);
        if (res == null) {
            log.debug("### 授权通过(当前资源未配置权限) [{}#{}] -> [{}]", targetClass, targetMethod, request.getRequestURI());
            return VoterResult.PASS;
        }
        if (Objects.equals(res.getEnableAuth(), EnumConstant.Permission_EnableAuth_0)) {
            log.debug("### 授权通过(当前资源不需要访问权限) [{}#{}] [{}] -> [{}]", targetClass, targetMethod, res.getApiPath(), request.getRequestURI());
            return VoterResult.PASS;
        }
        if (!securityContext.hasPermissions(res.getStrFlag())) {
            log.debug("### 授权失败(未授权) [{}#{}] [{}] -> [{}]", targetClass, targetMethod, res.getApiPath(), request.getRequestURI());
            return VoterResult.REJECT;
        }
        log.debug("### 授权通过(已授权) [{}#{}] [{}] -> [{}]", targetClass, targetMethod, res.getApiPath(), request.getRequestURI());
        return VoterResult.PASS;
    }
}
