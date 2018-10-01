package org.clever.security.init;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.annotation.UrlAuthorization;
import org.clever.security.client.WebPermissionClient;
import org.clever.security.config.SecurityConfig;
import org.clever.security.dto.request.WebPermissionInitReq;
import org.clever.security.dto.response.WebPermissionInitRes;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.model.WebPermissionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 初始化系统的所有Url权限
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-03-17 11:24 <br/>
 */
@Component
@Slf4j
public class InitSystemUrlPermission implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 是否初始化完成
     */
    private volatile boolean initFinish = false;
    private AtomicInteger count = new AtomicInteger(0);

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private RequestMappingHandlerMapping handlerMapping;
    @Autowired
    private WebPermissionClient webPermissionClient;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        count.addAndGet(1);
        log.info("### [系统权限初始化] 当前Spring容器初始化次数 {} ", count.get());
        if (count.get() < securityConfig.getWaitSpringContextInitCount()) {
            log.info("### [系统权限初始化] 等待Spring容器初始化次数 {} ", count.get());
            return;
        }
        if (initFinish) {
            log.info("### [系统权限初始化] 已经初始化完成，跳过。");
            return;
        }
        if (StringUtils.isBlank(securityConfig.getSysName())) {
            throw new RuntimeException("系统名称未配置");
        }
        initFinish = true;
        log.info("### [系统权限初始化] 开始初始化...");
        // 获取系统中所有的资源,并排序 - allPermission
        List<WebPermissionModel> allPermission = new ArrayList<>();
        Map<RequestMappingInfo, HandlerMethod> handlerMap = handlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMap.entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            // 获取URL路由信息
            allPermission.add(newPermissions(requestMappingInfo, handlerMethod));
        }
        Collections.sort(allPermission);

        WebPermissionInitReq req = new WebPermissionInitReq();
        req.setAllPermission(allPermission);
        WebPermissionInitRes res = webPermissionClient.initWebPermission(securityConfig.getSysName(), req);
        // 打印相应的日志
        if (log.isInfoEnabled()) {
            StringBuilder strTmp = new StringBuilder();
            strTmp.append("\r\n");
            strTmp.append("#=======================================================================================================================#\r\n");
            strTmp
                    .append("# 新增的权限配置如下(")
                    .append(res.getAddPermission().size())
                    .append("条):")
                    .append("\t\t---> ")
                    .append(securityConfig.getDefaultNeedAuthorization() ? "需要授权" : "不需要授权")
                    .append("\r\n");
            for (WebPermissionModel permission : res.getAddPermission()) {
                strTmp.append("#\t ").append(getPermissionStr(permission)).append("\r\n");
            }
            strTmp.append("# 数据库里无效的权限配置(").append(res.getNotExistPermission().size()).append("条):\r\n");
            for (WebPermissionModel permission : res.getNotExistPermission()) {
                strTmp.append("#\t ").append(getPermissionStr(permission)).append("\r\n");
            }
            strTmp.append("#=======================================================================================================================#");
            log.info(strTmp.toString());
        }
    }

    /**
     * 打印权限配置信息字符串
     */
    private String getPermissionStr(WebPermissionModel permission) {
        return String.format("| %1$s | [%2$s] [%3$s#%4$s] -> [%5$s]",
                permission.getSysName(),
                permission.getTitle(),
                permission.getTargetClass(),
                permission.getTargetMethod(),
                permission.getResourcesUrl());
    }

    /**
     * 创建一个 Permission
     */
    @SuppressWarnings("deprecation")
    private WebPermissionModel newPermissions(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
        // 提取MVC路由信息
        Class<?> beanType = handlerMethod.getBeanType();
        Method method = handlerMethod.getMethod();
        // 获取标题
        String titlePrefix = beanType.getSimpleName();
        String titleSuffix = method.getName();
        UrlAuthorization urlAuthorizationForClass = beanType.getAnnotation(UrlAuthorization.class);
        Api api = beanType.getAnnotation(Api.class);
        if (urlAuthorizationForClass != null && StringUtils.isNotBlank(urlAuthorizationForClass.title())) {
            titlePrefix = urlAuthorizationForClass.title();
        } else if (api != null && (StringUtils.isNotBlank(api.value()) || StringUtils.isNotBlank(api.description()))) {
            titlePrefix = StringUtils.isNotBlank(api.value()) ? api.value() : api.description();
        }
        UrlAuthorization urlAuthorizationForMethod = handlerMethod.getMethodAnnotation(UrlAuthorization.class);
        ApiOperation apiOperation = handlerMethod.getMethodAnnotation(ApiOperation.class);
        if (urlAuthorizationForMethod != null && StringUtils.isNotBlank(urlAuthorizationForMethod.title())) {
            titleSuffix = urlAuthorizationForMethod.title();
        } else if (apiOperation != null && StringUtils.isNotBlank(apiOperation.value())) {
            titleSuffix = apiOperation.value();
        }
        String title = String.format("[%1$s]-%2$s", titlePrefix, titleSuffix);
        // 获取资源说明
        String description = "系统自动生成的权限配置";
        if (urlAuthorizationForMethod != null && StringUtils.isNotBlank(urlAuthorizationForMethod.description())) {
            description = urlAuthorizationForMethod.description();
        } else if (apiOperation != null && StringUtils.isNotBlank(apiOperation.notes())) {
            description = apiOperation.notes();
        }
        // 获取资源字符串
        String permissionStr = "[auto]:" + UUID.randomUUID().toString();
        String permissionStrPrefix = "";
        if (urlAuthorizationForClass != null && StringUtils.isNotBlank(urlAuthorizationForClass.permissionStr())) {
            permissionStrPrefix = urlAuthorizationForClass.permissionStr();
        }
        if (urlAuthorizationForMethod != null && StringUtils.isNotBlank(urlAuthorizationForMethod.permissionStr())) {
            if (StringUtils.isBlank(permissionStrPrefix)) {
                permissionStr = urlAuthorizationForMethod.permissionStr();
            } else {
                permissionStr = String.format("[%1$s]%2$s", permissionStrPrefix, urlAuthorizationForMethod.permissionStr());
            }
        }
        // 获取方法参数签名
        StringBuilder methodParams = new StringBuilder();
        Class<?>[] paramTypes = method.getParameterTypes();
        for (Class<?> clzz : paramTypes) {
            if (methodParams.length() > 0) {
                methodParams.append(", ");
            }
            methodParams.append(clzz.getName());
        }
        // 请求Url
        StringBuilder resourcesUrl = new StringBuilder();
        Set<String> urlArray = requestMappingInfo.getPatternsCondition().getPatterns();
        for (String url : urlArray) {
            if (resourcesUrl.length() > 0) {
                resourcesUrl.append(" | ");
            }
            resourcesUrl.append(url);
        }
        // 构建权限信息
        WebPermissionModel permission = new WebPermissionModel();
        permission.setSysName(securityConfig.getSysName());
        permission.setPermissionStr(permissionStr);
        if (securityConfig.getDefaultNeedAuthorization() != null && securityConfig.getDefaultNeedAuthorization()) {
            permission.setNeedAuthorization(EnumConstant.Permission_NeedAuthorization_1);
        } else {
            permission.setNeedAuthorization(EnumConstant.Permission_NeedAuthorization_2);
        }
        permission.setResourcesType(EnumConstant.Permission_ResourcesType_1);
        permission.setTitle(title);
        permission.setTargetClass(beanType.getName());
        permission.setTargetMethod(method.getName());
        permission.setTargetMethodParams(methodParams.toString());
        permission.setResourcesUrl(resourcesUrl.toString());
        permission.setTargetExist(EnumConstant.WebPermission_targetExist_1);
        permission.setDescription(description);
        permission.setCreateAt(new Date());
        return permission;
    }
}
