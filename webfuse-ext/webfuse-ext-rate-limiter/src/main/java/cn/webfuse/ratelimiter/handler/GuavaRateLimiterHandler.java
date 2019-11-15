package cn.webfuse.ratelimiter.handler;

import cn.webfuse.ratelimiter.aop.RateLimiterInfo;
import com.google.common.util.concurrent.RateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用Guava实现的的RateLimiter
 */
public class GuavaRateLimiterHandler extends AbstractRateLimiterHandler {
    private static final Map<String, RateLimiter> RATE_LIMITER_MAP = new ConcurrentHashMap<>();

    private final ReentrantLock lock = new ReentrantLock();

    public boolean tryAcquire(RateLimiterInfo rateLimiterInfo) {
        RateLimiter rateLimiter = RATE_LIMITER_MAP.get(rateLimiterInfo.getKey());
        if (rateLimiter == null) {
            try {
                lock.lock();
                rateLimiter = RATE_LIMITER_MAP.get(rateLimiterInfo.getKey());
                if (rateLimiter == null) {
                    rateLimiter = RateLimiter.create(rateLimiterInfo.getPermitsPerSecond());
                    RATE_LIMITER_MAP.put(rateLimiterInfo.getKey(), rateLimiter);
                }
            } finally {
                lock.unlock();
            }
        }
        return rateLimiter.tryAcquire(rateLimiterInfo.getPermits(), rateLimiterInfo.getTimeout(), rateLimiterInfo.getTimeUnit());
    }

    public static Map<String, RateLimiter> getRateLimiterMap() {
        return RATE_LIMITER_MAP;
    }
}
