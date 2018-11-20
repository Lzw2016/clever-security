package org.clever.security.jwt.repository;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.jwt.service.GenerateKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-20 21:41 <br/>
 */
@Component
@Slf4j
public class LoginFailCountRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private GenerateKeyService generateKeyService;

    public long incrLoginFailCount(String username) {
        // TODO 必须设置过期时间
        String loginFailCountKey = generateKeyService.getLoginFailCountKey(username);
        Long count = redisTemplate.opsForValue().increment(loginFailCountKey, 1);
        return count == null ? 0 : count;
    }

    public void deleteLoginFailCount(String username) {
        String loginFailCountKey = generateKeyService.getLoginFailCountKey(username);
        redisTemplate.delete(loginFailCountKey);
    }

    public long getLoginFailCount(String username) {
        String loginFailCountKey = generateKeyService.getLoginFailCountKey(username);
        Long count = redisTemplate.opsForValue().increment(loginFailCountKey, 0);
        return count == null ? 0 : count;
    }
}
