package org.clever.security.embed.crypto;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/10 21:33 <br/>
 */
public class BCryptPasswordEncoder implements PasswordEncoder {
    private static final int MIN_LOG_ROUNDS = 4;
    private static final int MAX_LOG_ROUNDS = 31;

    private final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
    private final Log logger = LogFactory.getLog(getClass());
    private final int strength;
    private final SecureRandom random;

    public BCryptPasswordEncoder() {
        this(-1);
    }

    /**
     * @param strength 使用的对数取整，介于4到31之间
     */
    public BCryptPasswordEncoder(int strength) {
        this(strength, null);
    }

    /**
     * @param strength 使用的对数取整，介于4到31之间
     * @param random   要使用的安全随机实例
     */
    public BCryptPasswordEncoder(int strength, SecureRandom random) {
        if (strength != -1 && (strength < MIN_LOG_ROUNDS || strength > MAX_LOG_ROUNDS)) {
            throw new IllegalArgumentException("Bad strength");
        }
        this.strength = strength;
        this.random = random;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        String salt;
        if (strength > 0) {
            if (random != null) {
                salt = BCrypt.gensalt(strength, random);
            } else {
                salt = BCrypt.gensalt(strength);
            }
        } else {
            salt = BCrypt.gensalt();
        }
        return BCrypt.hashpw(rawPassword.toString(), salt);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.length() == 0) {
            logger.warn("Empty encoded password");
            return false;
        }

        if (!BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
            logger.warn("Encoded password does not look like BCrypt");
            return false;
        }

        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }
}
