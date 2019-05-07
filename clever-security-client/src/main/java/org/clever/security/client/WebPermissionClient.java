package org.clever.security.client;

import feign.QueryMap;
import org.clever.security.dto.request.WebPermissionInitReq;
import org.clever.security.dto.request.WebPermissionModelGetReq;
import org.clever.security.dto.response.WebPermissionInitRes;
import org.clever.security.entity.model.WebPermissionModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-24 19:14 <br/>
 */
@FeignClient(contextId = "org.clever.security.client.WebPermissionClient", name = "clever-security-server", path = "/api")
public interface WebPermissionClient {

    /**
     * 根据系统和Controller信息查询Web权限
     */
    @GetMapping("/web_permission")
    WebPermissionModel getWebPermissionModel(@QueryMap WebPermissionModelGetReq req);

    /**
     * 查询某个系统的所有Web权限
     */
    @GetMapping("/web_permission/{sysName}")
    List<WebPermissionModel> findAllWebPermissionModel(@PathVariable("sysName") String sysName);

    /**
     * 初始化某个系统的所有Web权限
     */
    @PostMapping("/web_permission/{sysName}")
    WebPermissionInitRes initWebPermission(@PathVariable("sysName") String sysName, @RequestBody WebPermissionInitReq req);
}
