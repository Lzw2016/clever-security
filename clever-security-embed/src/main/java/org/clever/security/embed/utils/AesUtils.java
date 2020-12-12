package org.clever.security.embed.utils;

import org.clever.common.utils.codec.CryptoUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;

import java.nio.charset.StandardCharsets;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/12 20:47 <br/>
 */
public class AesUtils {
    /**
     * 对称加密
     */
    public static String encode(String key, String iv, CharSequence rawPassword) {
        byte[] data = CryptoUtils.aesEncrypt(
                rawPassword.toString().getBytes(StandardCharsets.UTF_8),
                EncodeDecodeUtils.decodeHex(key),
                EncodeDecodeUtils.decodeHex(iv)
        );
        return EncodeDecodeUtils.encodeBase64(data);
    }

    /**
     * 对称解密
     */
    public static String decode(String key, String iv, CharSequence encodedPassword) {
        return CryptoUtils.aesDecrypt(
                EncodeDecodeUtils.decodeBase64(encodedPassword.toString()),
                EncodeDecodeUtils.decodeHex(key),
                EncodeDecodeUtils.decodeHex(iv)
        );
    }
}
