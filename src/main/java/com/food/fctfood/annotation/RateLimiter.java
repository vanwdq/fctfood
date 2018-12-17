package com.food.fctfood.annotation;

import java.lang.annotation.*;

/**
 * 限流注解
 */
@Target(ElementType.METHOD)//作用于方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    int limit() default 10;
    int timeout() default 1000;
}