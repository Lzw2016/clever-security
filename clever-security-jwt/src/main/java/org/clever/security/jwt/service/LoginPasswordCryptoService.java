package org.clever.security.jwt.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.codec.CryptoUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.security.jwt.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 登录密码加密-解密
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-11-01 10:42 <br/>
 */
@SuppressWarnings("Duplicates")
@Component
@Slf4j
public class LoginPasswordCryptoService {

    @Autowired
    private SecurityConfig securityConfig;

    /**
     * 登录密码 AES 加密
     *
     * @return Base64编码密码
     */
    public String reqAesEncrypt(String input) {
        SecurityConfig.AesKey aesKey = securityConfig.getLoginReqAesKey();
        if (aesKey == null) {
            throw new BusinessException("请先配置登录密码AesKey");
        }
        byte[] passwordData = input.getBytes();
        byte[] key = EncodeDecodeUtils.decodeHex(aesKey.getReqPasswordAesKey());
        byte[] iv = EncodeDecodeUtils.decodeHex(aesKey.getReqPasswordAesIv());
        return EncodeDecodeUtils.encodeBase64(CryptoUtils.aesEncrypt(passwordData, key, iv));
    }

    /**
     * 登录密码 AES 解密
     *
     * @param input Base64编码密码
     */
    public String reqAesDecrypt(String input) {
        SecurityConfig.AesKey aesKey = securityConfig.getLoginReqAesKey();
        if (aesKey == null) {
            throw new BusinessException("请先配置登录密码AesKey");
        }
        byte[] passwordData = EncodeDecodeUtils.decodeBase64(input);
        byte[] key = EncodeDecodeUtils.decodeHex(aesKey.getReqPasswordAesKey());
        byte[] iv = EncodeDecodeUtils.decodeHex(aesKey.getReqPasswordAesIv());
        return CryptoUtils.aesDecrypt(passwordData, key, iv);
    }
}