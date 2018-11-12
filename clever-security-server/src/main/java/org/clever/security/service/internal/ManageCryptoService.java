package org.clever.security.service.internal;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.codec.CryptoUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.security.config.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 管理系统敏感数据加密-解密
 * 作者： lzw<br/>
 * 创建时间：2018-11-12 9:29 <br/>
 */
@Component
@Slf4j
public class ManageCryptoService {

    @Autowired
    private GlobalConfig globalConfig;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 请求数据 AES 加密
     *
     * @return Base64编码密码
     */
    public String reqAesEncrypt(String input) {
        byte[] passwordData = input.getBytes();
        byte[] key = EncodeDecodeUtils.decodeHex(globalConfig.getReqAesKey());
        byte[] iv = EncodeDecodeUtils.decodeHex(globalConfig.getReqAesIv());
        return EncodeDecodeUtils.encodeBase64(CryptoUtils.aesEncrypt(passwordData, key, iv));
    }

    /**
     * 请求数据 AES 解密
     *
     * @param input Base64编码密码
     */
    public String reqAesDecrypt(String input) {
        byte[] passwordData = EncodeDecodeUtils.decodeBase64(input);
        byte[] key = EncodeDecodeUtils.decodeHex(globalConfig.getReqAesKey());
        byte[] iv = EncodeDecodeUtils.decodeHex(globalConfig.getReqAesIv());
        return CryptoUtils.aesDecrypt(passwordData, key, iv);
    }

    /**
     * 存储数据 加密
     */
    public String dbEncode(String input) {
        return bCryptPasswordEncoder.encode(input);
    }

    /**
     * 数据库加密数据匹配
     */
    public boolean dbMatches(String str1, String str2) {
        return bCryptPasswordEncoder.matches(str1, str2);
    }
}
