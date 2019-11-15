package cn.webfuse.ratelimiter.annotations;

import cn.webfuse.ratelimiter.exception.RateLimiterException;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RateLimiter {

    /**
     * 限流的key
     */
    String key() default "default";

    /**
     * 每秒限制数量
     */
    double permitsPerSecond() default 80;

    /**
     * 获取许可数量
     */
    int permits() default 1;

    /**
     * 获取许可超时时间
     */
    long timeout() default 0;

    /**
     * 获取许可超时时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 超过限流值时抛出的异常
     */
    Class<? extends RateLimiterException> exception() default RateLimiterException.class;
}