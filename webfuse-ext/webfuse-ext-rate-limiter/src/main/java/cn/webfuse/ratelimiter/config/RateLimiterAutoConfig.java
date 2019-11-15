package cn.webfuse.ratelimiter.config;

import cn.webfuse.core.kit.mapper.BeanMapper;
import cn.webfuse.ratelimiter.annotations.RateLimiter;
import cn.webfuse.ratelimiter.aop.RateLimiterAspect;
import cn.webfuse.ratelimiter.aop.RateLimiterInterceptorAdapter;
import cn.webfuse.ratelimiter.config.properties.RateLimiterProperties;
import cn.webfuse.ratelimiter.handler.GuavaRateLimiterHandler;
import cn.webfuse.ratelimiter.handler.RedisRateLimiterHandler;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.SingletonMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(RateLimiterProperties.class)
@ConditionalOnProperty(prefix = "webfuse.ratelimiter", name = "enabled")
public class RateLimiterAutoConfig {

    @Autowired
    private RateLimiterProperties rateLimiterProperties;

    @Bean
    @ConditionalOnProperty(prefix = "webfuse.ratelimiter", name = "type", havingValue = "guava")
    public GuavaRateLimiterHandler guavaRateLimiterHandler() {
        return new GuavaRateLimiterHandler();
    }

    @Bean
    @ConditionalOnProperty(prefix = "webfuse.ratelimiter", name = "type", havingValue = "redis")
    public RedisRateLimiterHandler redisRateLimiterHandler() {
        return new RedisRateLimiterHandler();
    }

    @Bean
    public RateLimiterAspect rateLimiterAspect() {
        return new RateLimiterAspect();
    }

    @Bean
    public RateLimiterInterceptorAdapter rateLimiterInterceptorAdapter() {

        List<RateLimiterProperties.RateLimiterRule> ruleList = rateLimiterProperties.getRateLimiterRules();

        Map<String, Map<String, Object>> res = new HashMap<>();
        for (RateLimiterProperties.RateLimiterRule rule : ruleList) {
            String url = rule.getUrl();
            res.put(url, BeanMapper.convertBeanToMap(rule));
        }
        return new RateLimiterInterceptorAdapter(res);
    }


}
