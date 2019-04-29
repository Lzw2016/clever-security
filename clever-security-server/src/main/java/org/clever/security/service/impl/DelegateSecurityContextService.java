package org.clever.security.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.service.ISecurityContextService;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-22 20:08 <br/>
 */
@Primary
@Transactional(readOnly = true)
@Service
@Slf4j
public class DelegateSecurityContextService implements ISecurityContextService {

    private final List<ISecurityContextService> sessionServiceList = new ArrayList<>();

    public DelegateSecurityContextService(SessionSecurityContextService cookieSessionService, JwtTokenSecurityContextService jwtTokenSessionService) {
        sessionServiceList.add(cookieSessionService);
        sessionServiceList.add(jwtTokenSessionService);
    }

    @Override
    public Map<String, SecurityContext> getSecurityContext(String sysName, String userName) {
        Map<String, SecurityContext> map = new HashMap<>();
        for (ISecurityContextService sessionService : sessionServiceList) {
            Map<String, SecurityContext> tmp = sessionService.getSecurityContext(sysName, userName);
            if (tmp != null) {
                map.putAll(tmp);
            }
        }
        return map;
    }

    @Override
    public SecurityContext getSecurityContext(String sessionId) {
        SecurityContext securityContext = null;
        for (ISecurityContextService sessionService : sessionServiceList) {
            SecurityContext tmp = sessionService.getSecurityContext(sessionId);
            if (tmp != null) {
                securityContext = tmp;
                break;
            }
        }
        return securityContext;
    }

    @Override
    public List<SecurityContext> reloadSecurityContext(String sysName, String userName) {
        List<SecurityContext> securityContextList = new ArrayList<>();
        for (ISecurityContextService sessionService : sessionServiceList) {
            List<SecurityContext> tmp = sessionService.reloadSecurityContext(sysName, userName);
            if (tmp != null) {
                securityContextList.addAll(tmp);
            }
        }
        return securityContextList;
    }

    @Override
    public Map<String, List<SecurityContext>> reloadSecurityContext(String userName) {
        Map<String, List<SecurityContext>> map = new HashMap<>();
        for (ISecurityContextService sessionService : sessionServiceList) {
            Map<String, List<SecurityContext>> tmp = sessionService.reloadSecurityContext(userName);
            if (tmp != null) {
                map.putAll(tmp);
            }
        }
        return map;
    }

    @Override
    public int forcedOffline(String sysName, String userName) {
        int count = 0;
        for (ISecurityContextService sessionService : sessionServiceList) {
            int tmp = sessionService.forcedOffline(sysName, userName);
            count += tmp;
        }
        return count;
    }

    @Override
    public void delSession(String userName) {
        for (ISecurityContextService sessionService : sessionServiceList) {
            sessionService.delSession(userName);
        }
    }

    @Override
    public void delSession(String sysName, String userName) {
        for (ISecurityContextService sessionService : sessionServiceList) {
            sessionService.delSession(sysName, userName);
        }
    }
}
