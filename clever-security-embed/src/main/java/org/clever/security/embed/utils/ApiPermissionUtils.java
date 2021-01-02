package org.clever.security.embed.utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.clever.security.annotation.UrlAuthorization;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.entity.EnumConstant;
import org.clever.security.model.auth.ApiPermissionEntity;
import org.clever.security.model.auth.ApiPermissionModel;
import org.clever.security.utils.PermissionStrFlagUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-02 11:19 <br/>
 */
public class ApiPermissionUtils {

    @SuppressWarnings("deprecation")
    public static ApiPermissionModel parseApiPermission(SecurityConfig securityConfig, List<ApiPermissionEntity> allApiPermissionList, RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
        // 提取MVC路由信息
        Class<?> beanType = handlerMethod.getBeanType();
        Method method = handlerMethod.getMethod();
        if (securityConfig.getIgnoreAuthClass().contains(beanType.getName())) {
            return null;
        }
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
        String strFlag = null;
        String permissionStrPrefix = "";
        if (urlAuthorizationForClass != null && StringUtils.isNotBlank(urlAuthorizationForClass.strFlag())) {
            permissionStrPrefix = urlAuthorizationForClass.strFlag();
        }
        if (urlAuthorizationForMethod != null && StringUtils.isNotBlank(urlAuthorizationForMethod.strFlag())) {
            if (StringUtils.isBlank(permissionStrPrefix)) {
                strFlag = urlAuthorizationForMethod.strFlag();
            } else {
                strFlag = String.format("[%1$s]%2$s", permissionStrPrefix, urlAuthorizationForMethod.strFlag());
            }
        }
        // 获取方法参数签名
        StringBuilder methodParamSb = new StringBuilder();
        Class<?>[] paramTypes = method.getParameterTypes();
        for (Class<?> clazz : paramTypes) {
            if (methodParamSb.length() > 0) {
                methodParamSb.append(", ");
            }
            methodParamSb.append(clazz.getName());
        }
        // 请求Url
        StringBuilder apiPath = new StringBuilder();
        Set<String> urlArray = requestMappingInfo.getPatternsCondition().getPatterns();
        boolean needAuth = false;
        for (String url : urlArray) {
            needAuth = needAuth || PathFilterUtils.isAuthorizationRequest(url, "", securityConfig);
            if (apiPath.length() > 0) {
                apiPath.append(" | ");
            }
            apiPath.append(url);
        }
        if (needAuth) {
            return null;
        }
        final String className = beanType.getName();
        final String methodName = method.getName();
        final String methodParams = methodParamSb.toString();
        // 设置当前数据库中的strFlag
        ApiPermissionEntity apiPermissionEntity = allApiPermissionList.stream()
                .filter(apiPermission -> Objects.equals(apiPermission.getClassName(), className)
                        && Objects.equals(apiPermission.getMethodName(), methodName)
                        && Objects.equals(apiPermission.getMethodParams(), methodParams))
                .findFirst().orElse(null);
        if (apiPermissionEntity != null && StringUtils.isBlank(strFlag)) {
            strFlag = apiPermissionEntity.getStrFlag();
        }
        // 生成随机的strFlag
        if (StringUtils.isBlank(strFlag)) {
            strFlag = PermissionStrFlagUtils.createStrFlag();
        }
        // 创建API权限对象
        ApiPermissionModel apiPermissionModel = new ApiPermissionModel();
        apiPermissionModel.setStrFlag(strFlag);
        apiPermissionModel.setTitle(title);
        apiPermissionModel.setEnableAuth(securityConfig.isDefaultEnableApiAuth() ? EnumConstant.Permission_EnableAuth_1 : EnumConstant.Permission_EnableAuth_0);
        apiPermissionModel.setDescription(description);
        apiPermissionModel.setClassName(className);
        apiPermissionModel.setMethodName(methodName);
        apiPermissionModel.setMethodParams(methodParams);
        apiPermissionModel.setApiPath(apiPath.toString());
        return apiPermissionModel;
    }
}
