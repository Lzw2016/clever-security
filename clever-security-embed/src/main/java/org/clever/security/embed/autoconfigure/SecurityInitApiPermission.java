package org.clever.security.embed.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.security.client.AuthSupportClient;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.GetAllApiPermissionReq;
import org.clever.security.dto.request.GetDomainReq;
import org.clever.security.dto.request.RegisterApiPermissionReq;
import org.clever.security.dto.response.GetAllApiPermissionRes;
import org.clever.security.dto.response.RegisterApiPermissionRes;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.utils.ApiPermissionUtils;
import org.clever.security.entity.Domain;
import org.clever.security.jackson2.CleverSecurityJackson2Module;
import org.clever.security.model.auth.ApiPermissionEntity;
import org.clever.security.model.auth.ApiPermissionModel;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/20 14:06 <br/>
 */
@Order
@Configuration
@EnableConfigurationProperties({SecurityConfig.class})
@Slf4j
public class SecurityInitApiPermission implements CommandLineRunner {
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    private final Environment environment;
    private final RequestMappingHandlerMapping handlerMapping;
    private final List<ObjectMapper> objectMapperList;
    private final LoginSupportClient loginSupportClient;
    private final AuthSupportClient authSupportClient;
    protected boolean initialized = false;

    public SecurityInitApiPermission(
            SecurityConfig securityConfig,
            Environment environment,
            RequestMappingHandlerMapping handlerMapping,
            ObjectProvider<List<ObjectMapper>> objectMapperList,
            LoginSupportClient loginSupportClient,
            AuthSupportClient authSupportClient) {
        this.securityConfig = securityConfig;
        this.environment = environment;
        this.handlerMapping = handlerMapping;
        this.objectMapperList = objectMapperList.getIfAvailable();
        this.loginSupportClient = loginSupportClient;
        this.authSupportClient = authSupportClient;
    }

    @Override
    public synchronized void run(String... args) {
        if (initialized) {
            return;
        }
        initialized = true;
        // 初始化 ObjectMapper
        JacksonMapper.getInstance().getMapper().registerModule(CleverSecurityJackson2Module.instance);
        if (objectMapperList != null) {
            for (ObjectMapper objectMapper : objectMapperList) {
                if (objectMapper == JacksonMapper.getInstance().getMapper()) {
                    continue;
                }
                objectMapper.registerModule(CleverSecurityJackson2Module.instance);
            }
        }
        // 打印当前服务所在域
        Domain domain = loginSupportClient.getDomain(new GetDomainReq(securityConfig.getDomainId()));
        if (domain == null) {
            log.error("服务启动失败", new IllegalArgumentException(String.format("配置security.domain-id=%s错误,domain不存在", securityConfig.getDomainId())));
            System.exit(-1);
        }
        String applicationName = environment.getProperty("spring.application.name");
        if (!Objects.equals(domain.getName(), applicationName)) {
            log.error("服务启动失败", new IllegalArgumentException(String.format("域名称和应用名不一致,域名称=[%s],spring.application.name=[%s]", domain.getName(), applicationName)));
            System.exit(-1);
        }
        log.info("### 当前系统domain信息 | domain-id={} | name={} | redis-name-space={}", domain.getId(), domain.getName(), domain.getRedisNameSpace());
        // 查询当前所有的API权限数据
        GetAllApiPermissionRes getAllApiPermissionRes = authSupportClient.getAllApiPermission(new GetAllApiPermissionReq(domain.getId()));
        List<ApiPermissionEntity> allApiPermissionList = getAllApiPermissionRes.getAllApiPermissionList();
        // 初始化API权限信息
        List<ApiPermissionModel> allApiPermission = new ArrayList<>();
        Map<RequestMappingInfo, HandlerMethod> handlerMap = handlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMap.entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            // 获取URL路由信息
            ApiPermissionModel apiPermissionModel = ApiPermissionUtils.parseApiPermission(securityConfig, allApiPermissionList, requestMappingInfo, handlerMethod);
            if (apiPermissionModel == null) {
                continue;
            }
            allApiPermission.add(apiPermissionModel);
        }
        RegisterApiPermissionReq req = new RegisterApiPermissionReq(securityConfig.getDomainId());
        req.setApiPermissionList(allApiPermission);
        RegisterApiPermissionRes res = authSupportClient.registerApiPermission(req);
        // 打印相应的日志
        if (log.isInfoEnabled()) {
            StringBuilder strTmp = new StringBuilder();
            strTmp.append("\r\n");
            strTmp.append("#=======================================================================================================================#\r\n");
            strTmp
                    .append("# 系统注册信息：").append(applicationName).append("(").append(securityConfig.isDefaultEnableApiAuth() ? "需要授权" : "不需要授权").append(")").append("\n")
                    .append("# 新增的权限配置如下(").append(res.getAddPermissionList().size()).append("条):").append("\r\n");
            for (ApiPermissionModel permission : res.getAddPermissionList()) {
                strTmp.append("#\t ").append(permissionToString(permission)).append("\r\n");
            }
            strTmp.append("# 数据库里无效的权限配置(").append(res.getNotExistPermissionList().size()).append("条):\r\n");
            for (ApiPermissionModel permission : res.getNotExistPermissionList()) {
                strTmp.append("#\t ").append(permissionToString(permission)).append("\r\n");
            }
            strTmp.append("#=======================================================================================================================#");
            log.info(strTmp.toString());
        }
    }

    /**
     * 打印权限配置信息字符串
     */
    private String permissionToString(ApiPermissionModel permission) {
        return String.format(
                "[%1$s] [%2$s#%3$s] -> [%4$s]",
                permission.getTitle(),
                permission.getClassName(),
                permission.getMethodName(),
                permission.getApiPath()
        );
    }
}
