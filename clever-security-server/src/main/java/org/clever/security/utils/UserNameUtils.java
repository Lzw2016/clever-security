package org.clever.security.utils;

import org.clever.common.utils.SnowFlake;

import java.util.Random;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/27 15:01 <br/>
 */
public class UserNameUtils {
    /**
     * 验证码可能出现的字符
     */
    private static final char[] CODE_SEQ = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    /**
     * 生成随机的字符串
     *
     * @param length 设置随机字符串的长度
     * @return 随机字符串
     */
    private static String getRandString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = CODE_SEQ[random.nextInt(CODE_SEQ.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 生成用户id
     */
    public static String generateUid() {
        return String.format("u%s", SnowFlake.SNOW_FLAKE.nextId());
    }

    /**
     * 生成用户LoginName
     */
    public static String generateLoginName() {
        // TODO 需要保证生成的uid与数据库中的不重复
        final int minLength = 6;
        final int maxLength = 12;
        int length = minLength + new Double(Math.random() * (maxLength - minLength + 1)).intValue();
        return getRandString(length);
    }
}
