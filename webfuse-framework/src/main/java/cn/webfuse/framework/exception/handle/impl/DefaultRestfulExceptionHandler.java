package cn.webfuse.framework.exception.handle.impl;

import cn.webfuse.framework.exception.handle.RestfulError;
import cn.webfuse.framework.exception.handle.RestfulErrorConverter;
import cn.webfuse.framework.exception.handle.RestfulErrorResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Restful异常处理分析器，需要再在配置文件中进行配置。
 *
 * @author Jesen
 */
@Slf4j
public class DefaultRestfulExceptionHandler extends AbstractHandlerExceptionResolver implements InitializingBean {

    private List<HttpMessageConverter<?>> allMessageConverters = null;

    private HttpMessageConverter<?>[] messageConverters = null;
    /**
     * RestfulError错误转换器
     */
    private RestfulErrorConverter<?> restfulErrorConverter;

    /**
     * RestfulError错误解析器
     */
    private RestfulErrorResolver restfulErrorResolver;

    public DefaultRestfulExceptionHandler(RestfulErrorConverter<?> restfulErrorConverter, RestfulErrorResolver restfulErrorResolver) {
        this.restfulErrorConverter = restfulErrorConverter;
        this.restfulErrorResolver = restfulErrorResolver;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ensureMessageConverters();
    }

    /**
     * 获得所有的MessageConverters
     */
    private void ensureMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        if (this.messageConverters != null && this.messageConverters.length > 0) {
            converters.addAll(CollectionUtils.arrayToList(this.messageConverters));
        }
        new HttpMessageConverterHelper().addDefaultMessageConverters(converters);
        this.allMessageConverters = converters;
    }

    /**
     * 继承自WebMvcConfigurationSupport，在SpringMVC默认的MessageConverters基础上添加自定义的MessageConverters
     */
    private static final class HttpMessageConverterHelper extends WebMvcConfigurationSupport {
        public void addDefaultMessageConverters(List<HttpMessageConverter<?>> converters) {
            addDefaultHttpMessageConverters(converters);
        }
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        RestfulErrorResolver resolver = getRestfulErrorResolver();
        //获得RestfulError实例
        RestfulError restfulError = resolver.resolveError(request, response, handler, ex);
        if (restfulError == null) {
            return null;
        }
        ModelAndView mav = null;
        try {
            mav = getModelAndView(request, response, handler, restfulError);
        } catch (Exception invocationEx) {
            log.error("Acquiring ModelAndView for Exception [" + ex + "] resulted in an exception.", invocationEx);
        }
        return mav;
    }

    private ModelAndView getModelAndView(HttpServletRequest request, HttpServletResponse response, Object handler, RestfulError restfulError) throws IOException {
        response.setStatus(restfulError.getStatus().value());
        Object body = restfulError;
        RestfulErrorConverter converter = getRestfulErrorConverter();
        if (converter != null) {
            //转换成对应的返回
            body = converter.convert(restfulError);
        }
        return handleResponseBody(body, request, response);
    }

    private ModelAndView handleResponseBody(Object body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }
        MediaType.sortByQualityValue(acceptedMediaTypes);

        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        Class<?> bodyType = body.getClass();
        List<HttpMessageConverter<?>> converters = this.allMessageConverters;

        if (converters != null) {
            for (MediaType acceptedMediaType : acceptedMediaTypes) {
                for (HttpMessageConverter messageConverter : converters) {
                    //如果能够使用对应的格式输出的话
                    if (messageConverter.canWrite(bodyType, acceptedMediaType)) {
                        messageConverter.write(body, acceptedMediaType, outputMessage);
                        return new ModelAndView();
                    }
                }
            }
        }

        if (log.isWarnEnabled()) {
            log.warn("Could not find HttpMessageConverter that supports return type [" + bodyType + "] and " + acceptedMediaTypes);
        }
        return null;
    }

    public void setMessageConverters(HttpMessageConverter<?>[] messageConverters) {
        this.messageConverters = messageConverters;
        ensureMessageConverters();
    }

    public void setRestfulErrorConverter(RestfulErrorConverter<?> restfulErrorConverter) {
        this.restfulErrorConverter = restfulErrorConverter;
    }

    public void setRestfulErrorResolver(RestfulErrorResolver restfulErrorResolver) {
        this.restfulErrorResolver = restfulErrorResolver;
    }

    public RestfulErrorConverter<?> getRestfulErrorConverter() {
        return restfulErrorConverter;
    }

    public RestfulErrorResolver getRestfulErrorResolver() {
        return restfulErrorResolver;
    }


}
