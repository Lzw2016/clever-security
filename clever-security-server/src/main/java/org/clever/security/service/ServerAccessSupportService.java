package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.ServerAccessSupportClient;
import org.clever.security.entity.ServerAccessToken;
import org.clever.security.mapper.ServerAccessTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/23 17:27 <br/>
 */
@Transactional
@Primary
@Service
@Slf4j
public class ServerAccessSupportService implements ServerAccessSupportClient {
    private final Map<Long, List<ServerAccessToken>> cache = new ConcurrentHashMap<>();

    @Autowired
    private ServerAccessTokenMapper serverAccessTokenMapper;

    public synchronized void reloadServerAccessToken() {
        List<ServerAccessToken> list = serverAccessTokenMapper.findAllEffective();
        Map<Long, List<ServerAccessToken>> map = new HashMap<>();
        for (ServerAccessToken token : list) {
            List<ServerAccessToken> tmp = map.computeIfAbsent(token.getDomainId(), along -> new ArrayList<>());
            tmp.add(token);
        }
        Set<Long> oldKeySet = cache.keySet();
        for (Long oldKey : oldKeySet) {
            if (!map.containsKey(oldKey)) {
                cache.remove(oldKey);
            }
        }
        cache.putAll(map);
    }

    @Override
    public List<ServerAccessToken> findAllEffectiveServerAccessToken(Long domainId) {
        return cache.computeIfAbsent(domainId, along -> Collections.emptyList());
    }
}
