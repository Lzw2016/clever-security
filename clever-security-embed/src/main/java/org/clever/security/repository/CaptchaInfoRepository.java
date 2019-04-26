package org.clever.security.repository;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.model.CaptchaInfo;
import org.clever.security.service.GenerateKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-20 19:57 <br/>
 */
@Component
@Slf4j
public class CaptchaInfoRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private GenerateKeyService generateKeyService;

    public void saveCaptchaInfo(CaptchaInfo captchaInfo) {
        String captchaInfoKey = generateKeyService.getCaptchaInfoKey(captchaInfo.getCode().toUpperCase(), captchaInfo.getImageDigest());
        redisTemplate.opsForValue().set(captchaInfoKey, captchaInfo, captchaInfo.getEffectiveTime(), TimeUnit.MILLISECONDS);
    }

    /**
     * 读取验证码，不存在返回null
     */
    public CaptchaInfo getCaptchaInfo(String code, String imageDigest) {
        String captchaInfoKey = generateKeyService.getCaptchaInfoKey(code.toUpperCase(), imageDigest);
        Object object = redisTemplate.opsForValue().get(captchaInfoKey);
        if (object instanceof CaptchaInfo) {
            return (CaptchaInfo) object;
        }
        return null;
    }

    public void deleteCaptchaInfo(String code, String imageDigest) {
        String captchaInfoKey = generateKeyService.getCaptchaInfoKey(code.toUpperCase(), imageDigest);
        redisTemplate.delete(captchaInfoKey);
    }
}
