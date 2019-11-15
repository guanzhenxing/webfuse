package cn.webfuse.ratelimiter.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "webfuse.ratelimiter")
@Data
public class RateLimiterProperties {
    /**
     * 是否开启
     */
    private boolean enabled = true;

    /**
     * 类型：guava，redis
     */
    private String type = "guava";

    /**
     * url-RateLimiterRule键值对
     */
    private List<RateLimiterRule> rateLimiterRules = new ArrayList<>();

    @Data
    public static class RateLimiterRule {

        private String url;

        private String method;

        /**
         * 每秒限制数量
         */
        private double permitsPerSecond = 80;
        /**
         * 获取许可数量
         */
        private int permits = 1;
        /**
         * 获取许可超时时间
         */
        private long timeout = 0;
        /**
         * 获取许可超时时间单位：NANOSECONDS，MICROSECONDS，MILLISECONDS，SECONDS，MINUTES，HOURS，DAYS
         */
        private String timeUnitName = "MILLISECONDS";
        /**
         * 超过限流值时抛出的异常的类
         */
        private String exceptionClass = "cn.webfuse.ratelimiter.exception.RateLimiterException";
    }
}
