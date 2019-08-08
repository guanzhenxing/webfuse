package cn.webfuse.framework.config;

import cn.webfuse.framework.config.properties.WebMvcProperties;
import cn.webfuse.framework.constant.BasicSystemCode;
import cn.webfuse.framework.core.exception.AbstractBizException;
import cn.webfuse.framework.exception.handle.impl.Default404ErrorController;
import cn.webfuse.framework.exception.handle.impl.DefaultRestfulErrorConverter;
import cn.webfuse.framework.exception.handle.impl.DefaultRestfulErrorResolver;
import cn.webfuse.framework.exception.handle.impl.DefaultRestfulExceptionHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.Servlet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@AutoConfigureBefore({WebMvcAutoConfig.class, WebMvcAutoConfiguration.class, ErrorMvcAutoConfiguration.class})
@EnableConfigurationProperties(WebMvcProperties.class)
@ServletComponentScan(basePackages = "cn.webfuse")
public class ErrorMvcAutoConfig {

    public static final String PROPERTIES_PREFIX = "webfuse.mvc";

    @Autowired
    private WebMvcProperties webMvcProperties;

    /**
     * errorViewResolvers
     */
    @Autowired(required = false)
    private List<ErrorViewResolver> errorViewResolvers;

    /**
     * serverProperties
     */
    private final ServerProperties serverProperties;

    /**
     * 构造函数
     *
     * @param serverProperties serverProperties
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ErrorMvcAutoConfig(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    /**
     * DefaultRestfulErrorController配置
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "restful-exception-handle.enabled", matchIfMissing = true)
    public Default404ErrorController defaultRestfulErrorController(ErrorAttributes errorAttributes) {
        Default404ErrorController default404ErrorController =
                new Default404ErrorController(errorAttributes, this.serverProperties.getError(), this.errorViewResolvers);

        default404ErrorController.setDefaultDocument(webMvcProperties.getRestfulExceptionHandle().getDefaultDocument());

        return default404ErrorController;
    }


    /**
     * 异常处理解析器
     */
    @Bean
    @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "restful-exception-handle.enabled", matchIfMissing = true)
    public DefaultRestfulExceptionHandler defaultHandlerRestfulExceptionResolver() {

        DefaultRestfulErrorResolver defaultRestfulErrorResolver = new DefaultRestfulErrorResolver(getExceptionMappingDefinitions());
        defaultRestfulErrorResolver.setLocaleResolver(localeResolver());
        defaultRestfulErrorResolver.setDefaultDocument(webMvcProperties.getRestfulExceptionHandle().getDefaultDocument());
        defaultRestfulErrorResolver.setShowDeveloperMessage(webMvcProperties.getRestfulExceptionHandle().isShowDeveloperMessage());

        DefaultRestfulErrorConverter defaultRestfulErrorConverter = new DefaultRestfulErrorConverter();


        DefaultRestfulExceptionHandler handlerRestfulExceptionResolver =
                new DefaultRestfulExceptionHandler(defaultRestfulErrorConverter, defaultRestfulErrorResolver);
        handlerRestfulExceptionResolver.setOrder(-1);

        return handlerRestfulExceptionResolver;
    }


    private List<DefaultRestfulErrorResolver.ExceptionDefinition> getExceptionMappingDefinitions() {


        //自定义的异常处理
        List<DefaultRestfulErrorResolver.ExceptionDefinition> customExceptionMappingDefinitions = getCustomExceptionMappingDefinitions();

        // 系统默认的，然后去除掉和自定义冲突的
        List<DefaultRestfulErrorResolver.ExceptionDefinition> defaultExceptionMappingDefinitions =
                createDefaultExceptionMappingDefinitions().stream().filter(demd -> {
                    AtomicBoolean flag = new AtomicBoolean(true);
                    customExceptionMappingDefinitions.stream().forEach(cemd -> {
                        if (demd.getExceptionClazz().equals(cemd.getExceptionClazz())) {
                            flag.set(false);
                            return;
                        }
                    });
                    return flag.get();
                }).collect(Collectors.toList());

        defaultExceptionMappingDefinitions.addAll(customExceptionMappingDefinitions);

        return defaultExceptionMappingDefinitions;

    }

    private List<DefaultRestfulErrorResolver.ExceptionDefinition> getCustomExceptionMappingDefinitions() {
        WebMvcProperties.RestfulExceptionHandle restfulExceptionHandle = webMvcProperties.getRestfulExceptionHandle();
        String defaultDocument = restfulExceptionHandle.getDefaultDocument();
        return restfulExceptionHandle.getMappings().stream()
                .map(mapping -> {
                    String document = StringUtils.isBlank(mapping.getDocument()) ? defaultDocument : mapping.getDocument();
                    return new DefaultRestfulErrorResolver.ExceptionDefinition(
                            mapping.getClazz(), mapping.getStatus(), mapping.getCode(),
                            mapping.getMessage(), mapping.getDeveloperMessage(), document);
                })
                .collect(Collectors.toList());
    }

    /**
     * 定义本地化
     */
    @Bean
    public LocaleResolver localeResolver() {
        //通过检验HTTP请求的accept-language头部来解析区域。
        return new AcceptHeaderLocaleResolver();
    }


    /**
     * 默认的异常和处理代码匹配
     *
     * @return
     */
    private List<DefaultRestfulErrorResolver.ExceptionDefinition> createDefaultExceptionMappingDefinitions() {

        List<DefaultRestfulErrorResolver.ExceptionDefinition> list = new ArrayList<>();
        // 400
        applyDef(list, HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST, BasicSystemCode.PARAMETER_ERROR.getCode());
        applyDef(list, MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST, BasicSystemCode.PARAMETER_ERROR.getCode());
        applyDef(list, TypeMismatchException.class, HttpStatus.BAD_REQUEST, BasicSystemCode.PARAMETER_ERROR.getCode());
        applyDef(list, "javax.validation.ValidationException", HttpStatus.BAD_REQUEST, BasicSystemCode.PARAMETER_ERROR.getCode());
        applyDef(list, BindException.class, HttpStatus.BAD_REQUEST, BasicSystemCode.PARAMETER_ERROR.getCode());
        applyDef(list, ServletRequestBindingException.class, HttpStatus.BAD_REQUEST, BasicSystemCode.PARAMETER_ERROR.getCode());
        applyDef(list, MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST, BasicSystemCode.PARAMETER_ERROR.getCode());
        applyDef(list, MissingServletRequestPartException.class, HttpStatus.BAD_REQUEST, BasicSystemCode.PARAMETER_ERROR.getCode());
        // 404
        applyDef(list, NoHandlerFoundException.class, HttpStatus.NOT_FOUND, BasicSystemCode.REQUEST_URI_NOT_FOUND.getCode());
        // 405
        applyDef(list, HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED, BasicSystemCode.REQUEST_METHOD_NOT_ALLOWED.getCode());
        // 406
        applyDef(list, HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE, BasicSystemCode.RESOURCE_NOT_ACCEPTABLE.getCode());
        // 409
        //can't use the class directly here as it may not be an available dependency:
        applyDef(list, "org.springframework.dao.DataIntegrityViolationException", HttpStatus.CONFLICT, BasicSystemCode.RESOURCE_CONFLICT.getCode());
        // 415
        applyDef(list, HttpMediaTypeNotSupportedException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE, BasicSystemCode.UNSUPPORTED_MEDIA_TYPE.getCode());
        // 500
        applyDef(list, Throwable.class, HttpStatus.INTERNAL_SERVER_ERROR, BasicSystemCode.SYSTEM_ERROR.getCode());
        applyDef(list, RuntimeException.class, HttpStatus.INTERNAL_SERVER_ERROR, BasicSystemCode.SYSTEM_ERROR.getCode());
        applyDef(list, MissingPathVariableException.class, HttpStatus.INTERNAL_SERVER_ERROR, BasicSystemCode.SYSTEM_ERROR.getCode());
        applyDef(list, ConversionNotSupportedException.class, HttpStatus.INTERNAL_SERVER_ERROR, BasicSystemCode.SYSTEM_ERROR.getCode());
        applyDef(list, HttpMessageNotWritableException.class, HttpStatus.INTERNAL_SERVER_ERROR, BasicSystemCode.SYSTEM_ERROR.getCode());
        //系统的基类
        applyDef(list, AbstractBizException.class, HttpStatus.INTERNAL_SERVER_ERROR, BasicSystemCode.SYSTEM_ERROR.getCode());

        //503
        applyDef(list, AsyncRequestTimeoutException.class, HttpStatus.SERVICE_UNAVAILABLE, BasicSystemCode.SERVICE_UNAVAILABLE.getCode());

        return list;
    }

    private void applyDef(List<DefaultRestfulErrorResolver.ExceptionDefinition> list, Class clazz, HttpStatus status, String code) {
        applyDef(list, clazz.getName(), status, code);
    }

    private void applyDef(List<DefaultRestfulErrorResolver.ExceptionDefinition> list, String name, HttpStatus status, String code) {

        DefaultRestfulErrorResolver.ExceptionDefinition exceptionDefinition =
                new DefaultRestfulErrorResolver.ExceptionDefinition(name, status.value(), code, "", "", "");
        list.add(exceptionDefinition);

    }
}
