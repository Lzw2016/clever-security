package org.clever.security;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/21 21:36 <br/>
 */
public interface PatternConstant {
    /**
     * 手机号正则
     */
    String Telephone_Pattern = "(?:0|86|\\+86)?1[3456789]\\d{9}";

    /**
     * 登录名称<br />
     * ^[a-zA-Z0-9\u4e00-\u9fa5()\[\]{}_-]{4,16}$
     */
    String LoginName_Pattern = "^[a-zA-Z0-9_-]{4,32}$";
}
