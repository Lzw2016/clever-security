package org.clever.security.embed.authorization.voter;

import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.exception.AuthorizationInnerException;
import org.clever.security.model.SecurityContext;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/06 20:27 <br/>
 */
public class ControllerAuthorizationVoter implements AuthorizationVoter {
    /**
     *
     */
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public ControllerAuthorizationVoter(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        Assert.notNull(requestMappingHandlerMapping, "参数requestMappingHandlerMapping不能为null");
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
        // TODO authorization

        return VoterResult.PASS;
    }
}
