package com.wcy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口防刷
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface AccessLimit {

    /**
     * 秒
     * @return  多少秒内
     */
    long second() default 5L;

    /**
     * 最大访问次数
     * @return 最大访问次数
     */
    long maxTime() default 3L;

    /**
     * 禁用时长，单位：秒
     * @return 禁用时长
     */
    long forbiddenTime() default 120L;
}
