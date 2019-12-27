package cn.webfuse.framework.config;

import cn.webfuse.framework.config.properties.WebMvcProperties;
import cn.webfuse.framework.context.SpringContextHolder;
import cn.webfuse.framework.web.method.UrlQueriesSnakeToCamelServletModelAttributeMethodProcessor;
import cn.webfuse.framework.web.version.ApiVersionRequestMappingHandlerMapping;
import cn.webfuse.framework.web.xss.XssFilter;
import cn.webfuse.framework.web.xss.XssJsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

/**
 * 自动WebMvc配置
 *
 * @author Jesen
 */
@Configuration
@EnableConfigurationProperties(WebMvcProperties.class)
@ServletComponentScan(basePackages = "cn.webfuse")
public class WebMvcAutoConfig {

    public static final String PROPERTIES_PREFIX = "webfuse.mvc";

    @Autowired
    private WebMvcProperties webMvcProperties;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    /**
     * 转换蛇形为驼峰型参数
     */
    @Bean
    @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "url-queries-snake-to-camel")
    public WebMvcConfigurer uriQueriesSnakeToCamelArgumentResolverConfig() {
        return new WebMvcConfigurer() {

            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
                argumentResolvers.add(customModelAttributeMethodProcessor());
            }

            @Bean
            protected UrlQueriesSnakeToCamelServletModelAttributeMethodProcessor customModelAttributeMethodProcessor() {
                return new UrlQueriesSnakeToCamelServletModelAttributeMethodProcessor(true, applicationContext);
            }
        };
    }

    /**
     * API版本控制
     */
    @Bean
    @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "api-version.enabled", matchIfMissing = true)
    public WebMvcRegistrations apiVersionConfig() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                ApiVersionRequestMappingHandlerMapping mapping = new ApiVersionRequestMappingHandlerMapping();
                mapping.setVersionPrefix(webMvcProperties.getApiVersion().getVersionPrefix());
                mapping.setMinimumVersion(webMvcProperties.getApiVersion().getMinimumVersion());
                mapping.setParsePackageVersion(webMvcProperties.getApiVersion().isParsePackageVersion());
                mapping.setVersionFlag(webMvcProperties.getApiVersion().getVersionFlag());
                return mapping;
            }
        };
    }

    /**
     * cors配置
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "cors.enabled", matchIfMissing = true)
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                List<WebMvcProperties.Cors.RegistrationConfig> registrationConfigList = webMvcProperties.getCors().getRegistrationConfig();

                registrationConfigList.stream().forEach(config -> {

                    CorsRegistration corsRegistration = registry.addMapping(config.getMapping());

                    if (config.getAllowCredentials() != null) {
                        corsRegistration.allowCredentials(config.getAllowCredentials());
                    }
                    if (StringUtils.isNotBlank(config.getAllowedOrigins())) {
                        corsRegistration.allowedOrigins(config.getAllowedOrigins().split(","));
                    }
                    if (StringUtils.isNotBlank(config.getAllowedMethods())) {
                        corsRegistration.allowedMethods(config.getAllowedMethods().split(","));
                    }
                    if (StringUtils.isNotBlank(config.getAllowedHeaders())) {
                        corsRegistration.allowedHeaders(config.getAllowedHeaders().split(","));
                    }
                    if (config.getMaxAge() > 0) {
                        corsRegistration.maxAge(config.getMaxAge());
                    }
                    if (StringUtils.isNotBlank(config.getExposedHeaders())) {
                        corsRegistration.allowedHeaders(config.getExposedHeaders().split(","));
                    }

                });
            }
        };
    }

    /**
     * xss过滤拦截器
     */
    @Bean
    @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "xss.enabled", matchIfMissing = true)
    public FilterRegistrationBean xssFilterRegistrationBean() {

        List<String> urlPatterns = webMvcProperties.getXss().getUrlPatterns();
        XssFilter xssFilter = new XssFilter();
        xssFilter.setUrlExclusion(webMvcProperties.getXss().getUrlExclusion());

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(xssFilter);
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns(urlPatterns.stream().toArray(String[]::new));
        filterRegistrationBean.setName(webMvcProperties.getXss().getFilterName());
        return filterRegistrationBean;
    }

    /**
     * 对RequestBody中的Json进行Xss过滤拦截，使用Jackson的Model
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "xss.enabled", matchIfMissing = true)
    public Module xssModule() {
        SimpleModule xssModule = new SimpleModule("xssModel");
        xssModule.addDeserializer(String.class, new XssJsonDeserializer());
        return xssModule;
    }


//类似这样的可以添加一些配置
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//
//        };
//    }


}
