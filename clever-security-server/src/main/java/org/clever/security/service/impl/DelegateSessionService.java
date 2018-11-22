package org.clever.security.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.service.ISessionService;
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
public class DelegateSessionService implements ISessionService {

    private final List<ISessionService> sessionServiceList = new ArrayList<>();

    public DelegateSessionService(
            CookieSessionService cookieSessionService,
            JwtTokenSessionService jwtTokenSessionService) {
        sessionServiceList.add(cookieSessionService);
        sessionServiceList.add(jwtTokenSessionService);
    }

    @Override
    public Map<String, SecurityContext> getSessionSecurityContext(String sysName, String userName) {
        Map<String, SecurityContext> map = new HashMap<>();
        for (ISessionService sessionService : sessionServiceList) {
            Map<String, SecurityContext> tmp = sessionService.getSessionSecurityContext(sysName, userName);
            if (tmp != null) {
                map.putAll(tmp);
            }
        }
        return map;
    }

    @Override
    public SecurityContext getSessionSecurityContext(String sessionId) {
        SecurityContext securityContext = null;
        for (ISessionService sessionService : sessionServiceList) {
            SecurityContext tmp = sessionService.getSessionSecurityContext(sessionId);
            if (tmp != null) {
                securityContext = tmp;
                break;
            }
        }
        return securityContext;
    }

    @Override
    public SecurityContext reloadSessionSecurityContext(String sysName, String userName) {
        SecurityContext securityContext = null;
        for (ISessionService sessionService : sessionServiceList) {
            SecurityContext tmp = sessionService.reloadSessionSecurityContext(sysName, userName);
            if (tmp != null) {
                securityContext = tmp;
                break;
            }
        }
        return securityContext;
    }

    @Override
    public Map<String, SecurityContext> reloadSessionSecurityContext(String userName) {
        Map<String, SecurityContext> map = new HashMap<>();
        for (ISessionService sessionService : sessionServiceList) {
            Map<String, SecurityContext> tmp = sessionService.reloadSessionSecurityContext(userName);
            if (tmp != null) {
                map.putAll(tmp);
            }
        }
        return map;
    }

    @Override
    public int forcedOffline(String sysName, String userName) {
        int count = 0;
        for (ISessionService sessionService : sessionServiceList) {
            int tmp = sessionService.forcedOffline(sysName, userName);
            count += tmp;
        }
        return count;
    }

    @Override
    public void delSession(String userName) {
        for (ISessionService sessionService : sessionServiceList) {
            sessionService.delSession(userName);
        }
    }

    @Override
    public void delSession(String sysName, String userName) {
        for (ISessionService sessionService : sessionServiceList) {
            sessionService.delSession(sysName, userName);
        }
    }
}
