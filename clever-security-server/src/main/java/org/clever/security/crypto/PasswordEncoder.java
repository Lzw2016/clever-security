package org.clever.security.crypto;

/**
 * 密码加密处理
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/12/10 21:29 <br/>
 */
public interface PasswordEncoder {
    /**
     * 加密密码
     *
     * @param rawPassword 原始密码
     * @return 加密之后的密码
     */
    String encode(CharSequence rawPassword);

    /**
     * 判断密码是否匹配
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return 匹配结果
     */
    boolean matches(CharSequence rawPassword, String encodedPassword);
}
