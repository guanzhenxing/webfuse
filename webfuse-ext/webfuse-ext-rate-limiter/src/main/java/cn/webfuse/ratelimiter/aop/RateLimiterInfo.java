package cn.webfuse.ratelimiter.aop;

import cn.webfuse.ratelimiter.exception.RateLimiterException;
import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class RateLimiterInfo {

    /**
     * key
     */
    private String key;
    /**
     * 每秒限制数量
     */
    private double permitsPerSecond;
    /**
     * 获取许可数量
     */
    private int permits;
    /**
     * 获取许可超时时间
     */
    private long timeout;
    /**
     * 获取许可超时时间单位：NANOSECONDS，MICROSECONDS，MILLISECONDS，SECONDS，MINUTES，HOURS，DAYS
     */
    private TimeUnit timeUnit;
    /**
     * 超过限流值时抛出的异常的类
     */
    private Class<? extends RateLimiterException> exception;
}
