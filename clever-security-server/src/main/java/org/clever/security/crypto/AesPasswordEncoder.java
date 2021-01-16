package org.clever.security.crypto;

import org.clever.common.utils.codec.CryptoUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 对称加密(支持解密)
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/12/12 20:15 <br/>
 */
public class AesPasswordEncoder implements PasswordEncoder {
    private final String key;
    private final String iv;

    public AesPasswordEncoder() {
        key = "55c95cc66fe4e0185a05ff2b7b5f2761";
        iv = "37df47c3bf7f889ca765c64d4ae76400";
    }

    public AesPasswordEncoder(String key, String iv) {
        this.key = key;
        this.iv = iv;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        byte[] data = CryptoUtils.aesEncrypt(
                rawPassword.toString().getBytes(StandardCharsets.UTF_8),
                EncodeDecodeUtils.decodeHex(key),
                EncodeDecodeUtils.decodeHex(iv)
        );
        return EncodeDecodeUtils.encodeBase64(data);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String eqPassword = encode(rawPassword);
        return Objects.equals(eqPassword, encodedPassword);
    }

    /**
     * 解密密码
     *
     * @param encodedPassword 加密后的密码
     * @return 原始密码
     */
    public String decode(CharSequence encodedPassword) {
        return CryptoUtils.aesDecrypt(
                EncodeDecodeUtils.decodeBase64(encodedPassword.toString()),
                EncodeDecodeUtils.decodeHex(key),
                EncodeDecodeUtils.decodeHex(iv)
        );
    }
}
