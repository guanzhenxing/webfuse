package cn.webfuse.ratelimiter.handler;

import cn.webfuse.ratelimiter.annotations.RateLimiter;

public class RedisRateLimiterHandler extends AbstractRateLimiterHandler {
    @Override
    public boolean tryAcquire(RateLimiter guavaRateLimiter) {

        //TODO 可以参考：https://gitee.com/-/ide/project/minbox-projects/api-boot/edit/master/-/api-boot-project/api-boot-plugins/api-boot-plugin-rate-limiter/src/main/java/org/minbox/framework/api/boot/plugin/rate/limiter/support/RedisLuaRateLimiter.java

        return false;
    }
}
