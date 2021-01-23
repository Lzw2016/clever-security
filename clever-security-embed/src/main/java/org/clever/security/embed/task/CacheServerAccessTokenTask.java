package org.clever.security.embed.task;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.ServerAccessSupportClient;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.entity.ServerAccessToken;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/23 18:40 <br/>
 */
@Slf4j
public class CacheServerAccessTokenTask {
    @Getter
    private final Map<String, List<ServerAccessToken>> cache = new ConcurrentHashMap<>();
    /**
     * 全局配置
     */
    private final SecurityConfig securityConfig;
    private final ServerAccessSupportClient serverAccessSupportClient;

    public CacheServerAccessTokenTask(SecurityConfig securityConfig, ServerAccessSupportClient serverAccessSupportClient) {
        Assert.notNull(securityConfig, "权限系统配置对象(SecurityConfig)不能为null");
        Assert.notNull(serverAccessSupportClient, "参数serverAccessSupportClient不能为null");
        this.securityConfig = securityConfig;
        this.serverAccessSupportClient = serverAccessSupportClient;
    }

    @Scheduled(initialDelay = 0, fixedDelay = 1000 * 8)
    public void reloadServerAccessToken() {
        long startTime = System.currentTimeMillis();
        log.debug("[刷新ServerAccessToken] - 开始...");
        try {
            doReloadServerAccessToken();
        } catch (Exception e) {
            log.debug("[刷新ServerAccessToken] - 失败", e);
        } finally {
            long useTime = System.currentTimeMillis() - startTime;
            log.debug("[刷新ServerAccessToken] - 完成，耗时: {}ms", useTime);
        }
    }

    protected synchronized void doReloadServerAccessToken() {
        List<ServerAccessToken> list = serverAccessSupportClient.findAllEffectiveServerAccessToken(securityConfig.getDomainId());
        Map<String, List<ServerAccessToken>> map = new HashMap<>();
        for (ServerAccessToken token : list) {
            List<ServerAccessToken> tmp = map.computeIfAbsent(token.getTokenName(), str -> new ArrayList<>());
            tmp.add(token);
        }
        Set<String> oldKeySet = cache.keySet();
        for (String oldKey : oldKeySet) {
            if (!map.containsKey(oldKey)) {
                cache.remove(oldKey);
            }
        }
        cache.putAll(map);
    }
}
