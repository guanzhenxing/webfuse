package cn.webfuse.framework.exception.handle.impl;

import cn.webfuse.core.constant.BaseErrorCode;
import cn.webfuse.framework.exception.BaseWebfuseException;
import cn.webfuse.framework.exception.handle.RestfulError;
import cn.webfuse.framework.exception.handle.RestfulErrorResolver;
import cn.webfuse.framework.kit.LocalHostInfoKits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
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
    private boolean showThrowable;
    private boolean showDeveloperMessage;
    private boolean showHostId;

    public DefaultRestfulErrorResolver(List<ExceptionDefinition> exceptionDefinitions) {
        this.exceptionMappings = definitionRestErrors(exceptionDefinitions);
    }


    @Override
    public RestfulError resolveError(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        /**
         * 获得运行时期的异常
         */
        RestfulError template = buildRuntimeError(ex);
        template.getDetail().setHostId(getHostId());
        template.getDetail().setRequestId(getRequestId(request));
        template.getDetail().setPath(request.getRequestURI());

        if (!showThrowable) {
            template.getDetail().setThrowable(null);
        }
        if (!showDeveloperMessage) {
            template.getDetail().setDeveloperMessage(null);
        }

        if (!showHostId) {
            template.getDetail().setHostId(null);
        }

        return template;
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
        //如果没有定义code，那么默认就是SYSTEM_ERROR
        if (StringUtils.isEmpty(code)) {
            code = "SYSTEM_ERROR";
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

        RestfulError.ErrorDetail errorDetail = new RestfulError.ErrorDetail();
        errorDetail.setDeveloperMessage(developerMessage);
        errorDetail.setDocument(document);

        RestfulError restfulError = new RestfulError(httpStatus, code, message, errorDetail);

        return restfulError;
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
            int s = BaseErrorCode.SYSTEM_ERROR.getStatus();
            String c = BaseErrorCode.SYSTEM_ERROR.getCode();
            String m = BaseErrorCode.SYSTEM_ERROR.getMessage();
            return new RestfulError(HttpStatus.valueOf(s), c, m, new RestfulError.ErrorDetail());
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
        String developerMessage = restfulError.getDetail().getDeveloperMessage();
        String document = restfulError.getDetail().getDocument();
        Map<String, Object> data = new HashMap<>();

        //如果异常继承于BaseWebfuseException时的处理
        if (ex instanceof BaseWebfuseException) {
            code = ((BaseWebfuseException) ex).getErrorCode().getCode();
            if (((BaseWebfuseException) ex).getErrorCode().getStatus() != 0) {
                httpStatus = HttpStatus.valueOf(((BaseWebfuseException) ex).getErrorCode().getStatus());
            } else {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            message = ((BaseWebfuseException) ex).getErrorCode().getMessage();
            developerMessage = StringUtils.join(ex.getStackTrace(), "\n");
            data = ((BaseWebfuseException) ex).getData();
        }

        if (StringUtils.isEmpty(message)) {
            message = BaseErrorCode.SYSTEM_ERROR.getMessage();
        }
        if (StringUtils.isEmpty(developerMessage)) {
            developerMessage = ex.toString();
        }
        if (StringUtils.isEmpty(document)) {
            document = this.defaultDocument;
        }


        RestfulError.ErrorDetail errorDetail = new RestfulError.ErrorDetail();
        errorDetail.setDeveloperMessage(developerMessage);
        errorDetail.setDocument(document);
        errorDetail.setData(MapUtils.isNotEmpty(data) ? data : null);
        errorDetail.setThrowable(ex);

        return new RestfulError(httpStatus, code, message, errorDetail);
    }

    //======================//

    private HttpStatus getStatus(RestfulError template) {
        return template.getStatus();
    }

    private String getCode(RestfulError template) {
        return template.getCode();
    }

    private String getDeveloperMessage(RestfulError template, HttpServletRequest request) {
        return getMessage(template.getDetail().getDeveloperMessage(), request);
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
        return template.getDetail().getDocument();
    }

    private String getHostId() {
        return LocalHostInfoKits.getLocalHost();
    }

    private String getRequestId(HttpServletRequest request) {
        return request.getHeader("X-Request-Id");
    }

    //======================//

    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    public void setDefaultDocument(String defaultDocument) {
        this.defaultDocument = defaultDocument;
    }

    public void setShowDeveloperMessage(boolean showDeveloperMessage) {
        this.showDeveloperMessage = showDeveloperMessage;
    }

    public void setShowThrowable(boolean showThrowable) {
        this.showThrowable = showThrowable;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setShowHostId(boolean showHostId) {
        this.showHostId = showHostId;
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