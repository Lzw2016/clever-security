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
        String str = "李志伟123abc!@#$";
        String key = "1234567890123456";
        byte[] iv = CryptoUtils.generateIV();
        String str2 = EncodeDecodeUtils.encodeHex(CryptoUtils.aesEncrypt(str.getBytes(), key.getBytes(), iv));
        log.info("加密 {}", str2);
        log.info("解密 {}", CryptoUtils.aesDecrypt(EncodeDecodeUtils.decodeHex(str2), key.getBytes(), iv));
    }
}
