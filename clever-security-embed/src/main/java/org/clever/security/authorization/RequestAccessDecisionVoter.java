package org.clever.security.authorization;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.WebPermissionClient;
import org.clever.security.config.SecurityConfig;
import org.clever.security.dto.request.WebPermissionModelGetReq;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.model.WebPermissionModel;
import org.clever.security.model.LoginUserDetails;
import org.clever.security.model.UserAuthority;
import org.clever.security.token.SecurityContextToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 基于HttpRequest授权校验
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-03-15 17:51 <br/>
 */
@Component
@Slf4j
public class RequestAccessDecisionVoter implements AccessDecisionVoter<FilterInvocation> {

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private WebPermissionClient webPermissionClient;
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    // 不去需要授权就能访问的Url
    private List<AntPathRequestMatcher> antPathRequestMatcherList = new ArrayList<>();

    @PostConstruct
    public void init() {
        if (securityConfig.getIgnoreAuthorizationUrls() != null) {
            for (String url : securityConfig.getIgnoreAuthorizationUrls()) {
                antPathRequestMatcherList.add(new AntPathRequestMatcher(url));
            }
            // 打印相应的日志
            if (log.isInfoEnabled()) {
                StringBuilder strTmp = new StringBuilder();
                strTmp.append("\r\n");
                strTmp.append("#=======================================================================================================================#\r\n");
                strTmp.append("不需要授权检查的资源:\r\n");
                for (String url : securityConfig.getIgnoreAuthorizationUrls()) {
                    strTmp.append("\t").append(url).append("\r\n");
                }
                strTmp.append("#=======================================================================================================================#");
                log.info(strTmp.toString());
            }
        }
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    /**
     * 授权实现
     */
    @Override
    public int vote(Authentication authentication, FilterInvocation filterInvocation, Collection<ConfigAttribute> attributes) {
        if (!(authentication instanceof SecurityContextToken)) {
            log.info("### 放弃授权(authentication类型不匹配SecurityContextToken) -> [{}] [{}]", authentication.getClass().getSimpleName(), filterInvocation.getRequestUrl());
            return ACCESS_ABSTAIN;
        }
        SecurityContextToken securityContextToken = (SecurityContextToken) authentication;
        LoginUserDetails loginUserDetails = securityContextToken.getUserDetails();
        if (loginUserDetails == null) {
            log.info("### 放弃授权(loginUserDetails为空) -> [{}]", filterInvocation.getRequestUrl());
            return ACCESS_ABSTAIN;
        }
        log.info("### 开始授权 [username={}] -> [{}]", loginUserDetails.getUsername(), filterInvocation.getRequestUrl());
        HandlerExecutionChain handlerExecutionChain;
        try {
            handlerExecutionChain = requestMappingHandlerMapping.getHandler(filterInvocation.getHttpRequest());
        } catch (Throwable e) {
            log.warn("### 授权时出现异常", e);
            return ACCESS_DENIED;
        }
        if (handlerExecutionChain == null) {
            log.info("### 授权通过(未知的资源404) -> [{}]", filterInvocation.getRequestUrl());
            return ACCESS_GRANTED;
        }
        // 匹配是否是不需要授权就能访问的Url
        for (AntPathRequestMatcher antPathRequestMatcher : antPathRequestMatcherList) {
            if (antPathRequestMatcher.matches(filterInvocation.getRequest())) {
                log.info("### 授权通过(不需要授权的URL) [{}] -> [{}]", antPathRequestMatcher.getPattern(), filterInvocation.getRequestUrl());
                return ACCESS_GRANTED;
            }
        }
        HandlerMethod handlerMethod = (HandlerMethod) handlerExecutionChain.getHandler();
        String targetClass = handlerMethod.getBeanType().getName();
        String targetMethod = handlerMethod.getMethod().getName();
        // 获取放签名
        StringBuilder methodParams = new StringBuilder();
        Class<?>[] paramTypes = handlerMethod.getMethod().getParameterTypes();
        for (Class<?> clzz : paramTypes) {
            if (methodParams.length() > 0) {
                methodParams.append(", ");
            }
            methodParams.append(clzz.getName());
        }
        WebPermissionModelGetReq req = new WebPermissionModelGetReq();
        req.setSysName(securityConfig.getSysName());
        req.setTargetClass(targetClass);
        req.setTargetMethod(targetMethod);
        req.setTargetMethodParams(methodParams.toString());
        WebPermissionModel webPermissionModel = webPermissionClient.getWebPermissionModel(req);
        if (webPermissionModel == null) {
            log.info("### 授权通过(当前资源未配置权限) [{}#{}] -> [{}]", targetClass, targetMethod, filterInvocation.getRequestUrl());
            return ACCESS_GRANTED;
        }
        log.info("### 权限字符串 [{}] -> [{}]", webPermissionModel.getPermissionStr(), filterInvocation.getRequestUrl());
        if (Objects.equals(webPermissionModel.getNeedAuthorization(), EnumConstant.Permission_NeedAuthorization_2)) {
            log.info("### 授权通过(当前资源不需要访问权限) [{}#{}] [{}] -> [{}]", targetClass, targetMethod, webPermissionModel.getResourcesUrl(), filterInvocation.getRequestUrl());
            return ACCESS_GRANTED;
        }
        // 对比权限字符串 permission.getPermission()
        if (checkPermission(webPermissionModel.getPermissionStr(), loginUserDetails.getAuthorities())) {
            log.info("### 授权通过(已授权) [{}#{}] [{}] -> [{}]", targetClass, targetMethod, webPermissionModel.getResourcesUrl(), filterInvocation.getRequestUrl());
            return ACCESS_GRANTED;
        }
        log.info("### 授权失败(未授权) [{}#{}] [{}] -> [{}]", targetClass, targetMethod, webPermissionModel.getResourcesUrl(), filterInvocation.getRequestUrl());
        return ACCESS_DENIED;
    }

    /**
     * 校验权限字符串
     */
    private boolean checkPermission(String permissionStr, Collection<UserAuthority> authorities) {
        if (authorities == null) {
            return false;
        }
        for (UserAuthority userAuthority : authorities) {
            if (Objects.equals(permissionStr, userAuthority.getAuthority())) {
                log.info("### 权限字符串匹配成功 [{}] [{}]", userAuthority.getAuthority(), userAuthority.getTitle());
                return true;
            }
        }
        return false;
    }
}
