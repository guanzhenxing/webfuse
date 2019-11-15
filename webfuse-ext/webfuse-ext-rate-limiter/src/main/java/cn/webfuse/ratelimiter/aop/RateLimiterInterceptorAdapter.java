package cn.webfuse.ratelimiter.aop;

import cn.webfuse.core.kit.ExceptionKits;
import cn.webfuse.core.kit.ObjectKits;
import cn.webfuse.core.kit.number.NumberKits;
import cn.webfuse.ratelimiter.handler.AbstractRateLimiterHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RateLimiterInterceptorAdapter extends HandlerInterceptorAdapter {

    @Autowired
    private AbstractRateLimiterHandler abstractRateLimiterHandler;

    private Map<String, Map<String, Object>> urlRateLimiterRule;

    public RateLimiterInterceptorAdapter(Map<String, Map<String, Object>> urlRateLimiterRule) {
        this.urlRateLimiterRule = urlRateLimiterRule;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String lookupPath = new UrlPathHelper().getLookupPathForRequest(request);
        PathMatcher matcher = new AntPathMatcher();

        urlRateLimiterRule.keySet().stream().forEach(url -> {
            boolean matched = matcher.match(url, lookupPath);
            if (matched) {
                Map<String, Object> rule = urlRateLimiterRule.get(url);
                String method = ObjectKits.toPrettyString(rule.get("method"));
                if (request.getMethod().equalsIgnoreCase(method)) {
                    RateLimiterInfo rateLimiterInfo = buildRateLimiterInfo(rule);
                    if (!abstractRateLimiterHandler.tryAcquire(rateLimiterInfo)) {
                        log.warn("[{}] has over limit", rateLimiterInfo.getKey());
                        if (rateLimiterInfo.getException() != null) {
                            try {
                                throw rateLimiterInfo.getException().getConstructor(String.class).newInstance("[{" + rateLimiterInfo.getKey() + "}] has over limit");
                            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                                ExceptionKits.unchecked(e);
                            }
                        }
                    }
                }
            }
        });
        return true;
    }

    private RateLimiterInfo buildRateLimiterInfo(Map<String, Object> stringObjectMap) {

        RateLimiterInfo rateLimiterInfo = new RateLimiterInfo();
        rateLimiterInfo.setKey(ObjectKits.toPrettyString(stringObjectMap.get("url")) + "_" + ObjectKits.toPrettyString(stringObjectMap.get("method")));
        rateLimiterInfo.setPermits(NumberKits.parseNumber(stringObjectMap.get("permits"), Integer.class));
        rateLimiterInfo.setPermitsPerSecond(NumberKits.parseNumber(stringObjectMap.get("permitsPerSecond"), Long.class));
        rateLimiterInfo.setTimeout(NumberKits.parseNumber(stringObjectMap.get("timeout"), Integer.class));

        TimeUnit timeUnit = EnumUtils.getEnum(TimeUnit.class, ObjectKits.toPrettyString(stringObjectMap.get("timeUnit")));
        rateLimiterInfo.setTimeUnit(timeUnit);

        try {
            Class exception = Class.forName(ObjectKits.toPrettyString(stringObjectMap.get("exception")));
            rateLimiterInfo.setException(exception);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return rateLimiterInfo;
    }
}
