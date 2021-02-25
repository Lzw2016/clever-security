package org.clever.security.utils;

import java.util.UUID;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-02 16:31 <br/>
 */
public class PermissionStrFlagUtils {
    public static final String AUTO_STR_FLAG = "[auto]";

    /**
     * 创建权限唯一字符串
     */
    public static String createStrFlag() {
        return String.format("%s-%s", AUTO_STR_FLAG, UUID.randomUUID().toString());
    }

    /**
     * 创建权限唯一字符串
     */
    public static String createStrFlag(String flag) {
        return String.format("%s-%s", flag, UUID.randomUUID().toString());
    }
}
