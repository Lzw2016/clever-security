package org.clever.security.test;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.reflection.ReflectionsUtils;
import org.clever.security.session.config.SecurityConfig;
import org.clever.security.session.service.LoginPasswordCryptoService;
import org.junit.Test;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-11 21:00 <br/>
 */
@Slf4j
public class LoginPasswordCryptoServiceTest {

    @Test
    public void t01() {
        LoginPasswordCryptoService cryptoService = new LoginPasswordCryptoService();
        SecurityConfig securityConfig = new SecurityConfig();
        SecurityConfig.AesKey aesKey = new SecurityConfig.AesKey();
        aesKey.setReqPasswordAesKey("636c657665722d736563757288888888");
        aesKey.setReqPasswordAesIv("636c657665722d736563757266666666");
        ReflectionsUtils.setFieldValue(securityConfig, "loginReqAesKey", aesKey);
        ReflectionsUtils.setFieldValue(cryptoService, "securityConfig", securityConfig);
        String tmp = cryptoService.reqAesEncrypt("lizhiwei");
        log.info("### {} {}", tmp, cryptoService.reqAesDecrypt(tmp));
    }
}
