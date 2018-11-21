package org.clever.security.jwt.repository;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.jwt.service.GenerateKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-20 21:41 <br/>
 */
@SuppressWarnings("UnusedReturnValue")
@Component
@Slf4j
public class LoginFailCountRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private GenerateKeyService generateKeyService;

    public long incrementLoginFailCount(String username) {
        String loginFailCountKey = generateKeyService.getLoginFailCountKey(username);
        Long count = redisTemplate.boundValueOps(loginFailCountKey).increment(1);
        redisTemplate.expire(loginFailCountKey, 10, TimeUnit.MINUTES);
        return count == null ? 0 : count;
    }

    public void deleteLoginFailCount(String username) {
        String loginFailCountKey = generateKeyService.getLoginFailCountKey(username);
        redisTemplate.delete(loginFailCountKey);
    }

    public long getLoginFailCount(String username) {
        String loginFailCountKey = generateKeyService.getLoginFailCountKey(username);
        Boolean exists = redisTemplate.hasKey(loginFailCountKey);
        if (exists != null && !exists) {
            return 0L;
        }
        Long count = redisTemplate.boundValueOps(loginFailCountKey).increment(0);
        return count == null ? 0 : count;
    }
}
