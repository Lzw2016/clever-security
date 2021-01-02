package org.clever.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义Url授权信息
 * 作者： lzw<br/>
 * 创建时间：2018-09-20 17:49 <br/>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UrlAuthorization {
    /**
     * 权限标题
     */
    String title() default "";

    /**
     * 权限唯一字符串标识(仅支持Method,不支持Class)
     */
    String strFlag() default "";

    /**
     * 权限说明(仅支持Method,不支持Class)
     */
    String description() default "";
}
