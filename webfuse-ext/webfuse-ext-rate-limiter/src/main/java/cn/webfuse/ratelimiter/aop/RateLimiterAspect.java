package cn.webfuse.ratelimiter.aop;

import cn.webfuse.ratelimiter.annotations.RateLimiter;
import cn.webfuse.ratelimiter.handler.AbstractRateLimiterHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

@Aspect
@Slf4j
public class RateLimiterAspect {

    @Autowired
    private AbstractRateLimiterHandler abstractRateLimiterHandler;

    @Pointcut("@annotation(cn.webfuse.ratelimiter.annotations.RateLimiter)")
    private void rateLimiterPoint() {
    }

    @Around("rateLimiterPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
        RateLimiterInfo rateLimiterInfo = buildRateLimiterInfo(rateLimiter);
        if (!abstractRateLimiterHandler.tryAcquire(rateLimiterInfo)) {
            log.warn("[{}] has over limit", rateLimiter.key());
            if (rateLimiter.exception() != null) {
                throw rateLimiter.exception().getConstructor(String.class).newInstance("[{" + rateLimiter.key() + "}] has over limit");
            }
            return null;
        }

        try {
            return pjp.proceed();
        } catch (Exception e) {
            log.error("execute rate limiter method [" + rateLimiter.key() + "] occurred an exception", e);
        }
        return null;
    }

    private RateLimiterInfo buildRateLimiterInfo(RateLimiter rateLimiter) {

        RateLimiterInfo rateLimiterInfo = new RateLimiterInfo();
        rateLimiterInfo.setKey(rateLimiter.key());
        rateLimiterInfo.setException(rateLimiter.exception());
        rateLimiterInfo.setPermits(rateLimiter.permits());
        rateLimiterInfo.setPermitsPerSecond(rateLimiter.permitsPerSecond());
        rateLimiterInfo.setTimeout(rateLimiter.timeout());
        rateLimiterInfo.setTimeUnit(rateLimiter.timeUnit());

        return rateLimiterInfo;
    }

}
