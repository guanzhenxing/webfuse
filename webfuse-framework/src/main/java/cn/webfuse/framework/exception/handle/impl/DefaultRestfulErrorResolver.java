package cn.webfuse.framework.exception.handle.impl;

import cn.webfuse.core.exception.AbstractBizException;
import cn.webfuse.framework.exception.handle.RestfulError;
import cn.webfuse.framework.exception.handle.RestfulErrorResolver;
import cn.webfuse.framework.kit.LocalHostInfoKits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 默认的RestfulError解析器。
 *
 * @author Jesen
 */
@Slf4j
public class DefaultRestfulErrorResolver implements RestfulErrorResolver, MessageSourceAware {


    public static final String DEFAULT_MESSAGE_VALUE = "";
    public static final String DEFAULT_EXCEPTION_MESSAGE_VALUE = "";
    /**
     * 异常对应
     */
    private Map<String, RestfulError> exceptionMappings;

    /**
     * 以下两个为国际化做准备
     */
    private MessageSource messageSource;
    private LocaleResolver localeResolver;

    private String defaultDocument;
    private boolean showDeveloperMessage;

    public DefaultRestfulErrorResolver(List<ExceptionDefinition> exceptionDefinitions) {
        this.exceptionMappings = definitionRestErrors(exceptionDefinitions);
    }

    public void setDefaultDocument(String defaultDocument) {
        this.defaultDocument = defaultDocument;
    }

    public void setShowDeveloperMessage(boolean showDeveloperMessage) {
        this.showDeveloperMessage = showDeveloperMessage;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @Override
    public RestfulError resolveError(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        /**
         * 获得运行时期的异常
         */
        RestfulError template = buildRuntimeError(ex);

        RestfulError.Builder builder = new RestfulError.Builder();
        builder.setThrowable(ex);
        builder.setStatus(getStatus(template));
        builder.setCode(getCode(template));
        builder.setMessage(getMessage(template, request));
        builder.setRequestId(getRequestId(request));
        builder.setHostId(getHostId());
        builder.setDocument(getDocument(template));
        if (showDeveloperMessage) {
            builder.setDeveloperMessage(getDeveloperMessage(template, request));
        }


        return builder.build();
    }


    /**
     * 转换成为：异常类-RestfulError格式的对应map
     *
     * @param definitions
     * @return
     */
    private Map<String, RestfulError> definitionRestErrors(List<ExceptionDefinition> definitions) {

        if (CollectionUtils.isEmpty(definitions)) {
            return Collections.emptyMap();
        }

        Map<String, RestfulError> res = new LinkedHashMap<>(definitions.size());

        definitions.stream().forEach(definition -> {
            //异常的类名
            String clazzName = definition.getExceptionClazz();
            //定义好的异常的处理格式
            RestfulError template = definitionRestError(definition);
            res.put(clazzName, template);
        });
        return res;
    }

    /**
     * 构造一个RestfulError。
     * 所有的错误定义都在此
     *
     * @param definition
     * @return
     */
    private RestfulError definitionRestError(ExceptionDefinition definition) {

        String code = definition.getCode();
        //如果没有定义code，那么默认就是INTERNAL_SERVER_ERROR
        if (StringUtils.isEmpty(code)) {
            code = "INTERNAL_SERVER_ERROR";
        }

        String message = definition.getMessage();
        if (StringUtils.isEmpty(message)) {
            //如果没有定义message,默认为 ""
            message = DEFAULT_MESSAGE_VALUE;
        }

        String status = String.valueOf(definition.getStatus());
        HttpStatus httpStatus;
        try {
            httpStatus = HttpStatus.valueOf(Integer.valueOf(status));
        } catch (Exception e) {
            //默认使用 HttpStatus.INTERNAL_SERVER_ERROR
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String developerMessage = definition.getDeveloperMessage();
        if (StringUtils.isEmpty(message)) {
            //如果没有定义developerMessage,默认为 ""
            developerMessage = DEFAULT_EXCEPTION_MESSAGE_VALUE;
        }

        String document = definition.getDocument();
        if (StringUtils.isEmpty(document)) {
            document = this.defaultDocument;
        }

        RestfulError.Builder builder = new RestfulError.Builder();
        builder.setCode(code);
        builder.setMessage(message);
        builder.setStatus(httpStatus);
        builder.setDeveloperMessage(developerMessage);
        builder.setDocument(document);

        return builder.build();
    }

    /**
     * 所有的异常数据构建都在此方法中进行
     *
     * @param ex
     * @return
     */
    private RestfulError buildRuntimeError(Exception ex) {

        Map<String, RestfulError> mappings = this.exceptionMappings;
        if (CollectionUtils.isEmpty(mappings)) {
            return null;
        }
        if (ex == null) {
            //如果为空，就直接用RuntimeException
            ex = new RuntimeException();
        }

        //获得异常的类
        Class clazz = ex.getClass();
        //获得定义好的异常的处理格式
        RestfulError restfulError = mappings.get(clazz.getName());
        //如果没有定义，那么就一直追溯它的父类，直到获得定义的格式为止
        if (restfulError == null) {
            List<Class<?>> superClasses = ClassUtils.getAllSuperclasses(clazz);
            for (Class superClass : superClasses) {
                restfulError = mappings.get(superClass.getName());
                if (restfulError != null) {
                    break;
                }
            }
        }

        //获得异常业务代码
        String code = restfulError.getCode();
        //获得异常的HTTP代码
        HttpStatus httpStatus = restfulError.getStatus();
        String message = restfulError.getMessage();
        String developerMessage = restfulError.getDeveloperMessage();
        String document = restfulError.getDocument();

        //如果异常继承于AbstractBizException时的处理
        if (ex instanceof AbstractBizException) {
            code = ((AbstractBizException) ex).getCode();
            if (((AbstractBizException) ex).getStatus() != 0) {
                httpStatus = HttpStatus.valueOf(((AbstractBizException) ex).getStatus());
            } else {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            message = ex.getMessage();
            developerMessage = ((AbstractBizException) ex).getDeveloperMessage();
            if (StringUtils.isEmpty(developerMessage)) {
                developerMessage = StringUtils.join(ex.getStackTrace(), "\n");
            }
        }

        if (StringUtils.isEmpty(message)) {
            message = ex.getMessage();
        }
        if (StringUtils.isEmpty(developerMessage)) {
            developerMessage = ex.toString();
        }
        if (StringUtils.isEmpty(document)) {
            document = this.defaultDocument;
        }

        RestfulError.Builder builder = new RestfulError.Builder();

        builder.setStatus(httpStatus);
        builder.setCode(code);
        builder.setMessage(message);
        builder.setDeveloperMessage(developerMessage);
        builder.setThrowable(ex);
        builder.setDocument(document);

        return builder.build();
    }

    private HttpStatus getStatus(RestfulError template) {
        return template.getStatus();
    }

    private String getCode(RestfulError template) {
        return template.getCode();
    }

    private String getDeveloperMessage(RestfulError template, HttpServletRequest request) {
        return getMessage(template.getDeveloperMessage(), request);
    }

    private String getMessage(RestfulError template, HttpServletRequest request) {
        return getMessage(template.getMessage(), request);
    }

    private String getMessage(String message, HttpServletRequest request) {
        if (message != null) {
            if ("null".equalsIgnoreCase(message) || "off".equalsIgnoreCase(message)) {
                return "";
            }
            if (messageSource != null) {
                Locale locale = null;
                if (localeResolver != null) {
                    locale = localeResolver.resolveLocale(request);
                } else {
                    locale = LocaleContextHolder.getLocale();
                }
                message = messageSource.getMessage(message, null, message, locale);
            }
        }
        return message;
    }

    private String getDocument(RestfulError template) {
        return template.getDocument();
    }


    private String getHostId() {
        return LocalHostInfoKits.getLocalHost();
    }

    private String getRequestId(HttpServletRequest request) {
        return request.getHeader("X-Request-Id");
    }

    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @Data
    @AllArgsConstructor
    public static class ExceptionDefinition {
        private String exceptionClazz;
        private Integer status;
        private String code;
        private String message;
        private String developerMessage;
        private String document;
    }
}