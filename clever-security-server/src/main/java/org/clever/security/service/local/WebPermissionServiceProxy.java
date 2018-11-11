package org.clever.security.service.local;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.WebPermissionClient;
import org.clever.security.dto.request.WebPermissionInitReq;
import org.clever.security.dto.request.WebPermissionModelGetReq;
import org.clever.security.dto.response.WebPermissionInitRes;
import org.clever.security.entity.model.WebPermissionModel;
import org.clever.security.service.WebPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-11 19:30 <br/>
 */
@Component
@Slf4j
public class WebPermissionServiceProxy implements WebPermissionClient {
    @Autowired
    private WebPermissionService webPermissionService;

    @Override
    public WebPermissionModel getWebPermissionModel(WebPermissionModelGetReq req) {
        return webPermissionService.getWebPermissionModel(req);
    }

    @Override
    public List<WebPermissionModel> findAllWebPermissionModel(String sysName) {
        return webPermissionService.findAllWebPermissionModel(sysName);
    }

    @Override
    public WebPermissionInitRes initWebPermission(String sysName, WebPermissionInitReq req) {
        return webPermissionService.initWebPermission(sysName, req);
    }
}
