package cn.webfuse.framework.web.method;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import javax.servlet.ServletRequest;

/**
 * 自定义请求参数模型处理。主要用于url参数的转换，对于requestBody无效
 *
 * @author Jesen
 */
public class UrlQueriesSnakeToCamelServletModelAttributeMethodProcessor extends ServletModelAttributeMethodProcessor implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public UrlQueriesSnakeToCamelServletModelAttributeMethodProcessor(boolean annotationNotRequired, ApplicationContext applicationContext) {
        super(annotationNotRequired);
        this.applicationContext = applicationContext;
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        UrlQueriesSnakeToCamelServletRequestDataBinder camelBinder = new UrlQueriesSnakeToCamelServletRequestDataBinder(binder.getTarget(), binder.getObjectName());
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
        requestMappingHandlerAdapter.getWebBindingInitializer().initBinder(camelBinder);
        camelBinder.bind(request.getNativeRequest(ServletRequest.class));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}