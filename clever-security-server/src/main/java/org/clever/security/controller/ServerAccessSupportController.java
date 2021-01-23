package org.clever.security.controller;

import org.clever.security.client.ServerAccessSupportClient;
import org.clever.security.entity.ServerAccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/23 17:21 <br/>
 */
@RestController
@RequestMapping("/security/api")
public class ServerAccessSupportController implements ServerAccessSupportClient {
    /**
     * 获取所有有效的ServerAccessToken
     */
    @GetMapping("/server_access_token/all_effective")
    @Override
    public List<ServerAccessToken> findAllEffectiveServerAccessToken(@RequestParam("domainId") Long domainId) {
        return null;
    }
}
