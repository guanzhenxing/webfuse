package cn.webfuse.ratelimiter.handler;

import cn.webfuse.ratelimiter.aop.RateLimiterInfo;

public abstract class AbstractRateLimiterHandler {

    public abstract boolean tryAcquire(RateLimiterInfo rateLimiterInfo);

}
