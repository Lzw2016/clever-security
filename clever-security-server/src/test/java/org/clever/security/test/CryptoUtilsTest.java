package org.clever.security.test;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.codec.CryptoUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.junit.Test;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-08 10:25 <br/>
 */
@Slf4j
public class CryptoUtilsTest {

    @Test
    public void t01() {
        log.info("{}", EncodeDecodeUtils.encodeHex("clever-security-server李志伟".getBytes()));


        String str = "李志伟123abc!@#$";
        byte[] key = EncodeDecodeUtils.decodeHex("636c657665722d73656375726974792d");
        byte[] iv = EncodeDecodeUtils.decodeHex("f0021ea5a06d5a7bade961afe47e9ad9");

        byte[] data = CryptoUtils.aesEncrypt(str.getBytes(), key, iv);

        String str2 = EncodeDecodeUtils.encodeBase64(data);
        log.info("加密 {}", str2);
        log.info("解密 {}", CryptoUtils.aesDecrypt(data, key, iv));
    }
}
