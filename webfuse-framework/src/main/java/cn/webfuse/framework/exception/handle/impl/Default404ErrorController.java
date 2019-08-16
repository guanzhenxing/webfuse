package cn.webfuse.framework.exception.handle.impl;

import cn.webfuse.framework.exception.BaseErrorCode;
import cn.webfuse.framework.kit.LocalHostInfoKits;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 主要为了404错误处理
 * <p>
 * 参考：https://my.oschina.net/wangkang80/blog/1523308
 *
 * @author Jesen
 */
@RequestMapping(value = "${server.error.path:${error.path:/error}}")
public class Default404ErrorController extends AbstractErrorController {

    private final ErrorProperties errorProperties;

    private String defaultDocument;

    /**
     * Create a new {@link BasicErrorController} instance.
     *
     * @param errorAttributes the error attributes
     * @param errorProperties configuration properties
     */
    public Default404ErrorController(ErrorAttributes errorAttributes,
                                     ErrorProperties errorProperties) {
        this(errorAttributes, errorProperties, Collections.emptyList());
    }

    public void setDefaultDocument(String defaultDocument) {
        this.defaultDocument = defaultDocument;
    }

    /**
     * Create a new {@link BasicErrorController} instance.
     *
     * @param errorAttributes    the error attributes
     * @param errorProperties    configuration properties
     * @param errorViewResolvers error view resolvers
     */
    public Default404ErrorController(ErrorAttributes errorAttributes,
                                     ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorViewResolvers);
        Assert.notNull(errorProperties, "ErrorProperties must not be null");
        this.errorProperties = errorProperties;
    }

    @Override
    public String getErrorPath() {
        return this.errorProperties.getPath();
    }

    /**
     * html错误，使用BasicErrorController的
     */
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request,
                                  HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(
                request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
        response.setStatus(status.value());
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);
    }

    @RequestMapping
    public ResponseEntity<DefaultRestfulErrorVO> error(HttpServletRequest request) {

        DefaultRestfulErrorVO vo = new DefaultRestfulErrorVO();
        vo.setCode(BaseErrorCode.REQUEST_URI_NOT_FOUND.getCode());
        vo.setMessage(BaseErrorCode.REQUEST_URI_NOT_FOUND.getMessage());

        vo.getDetail().put("hostId", LocalHostInfoKits.getLocalHost());
        vo.getDetail().put("document", this.defaultDocument);
        vo.getDetail().put("requestId", request.getHeader("X-Request-Id"));

        return new ResponseEntity<>(vo, HttpStatus.NOT_FOUND);
    }

    /**
     * Determine if the stacktrace attribute should be included.
     *
     * @param request  the source request
     * @param produces the media type produced (or {@code MediaType.ALL})
     * @return if the stacktrace attribute should be included
     */
    protected boolean isIncludeStackTrace(HttpServletRequest request,
                                          MediaType produces) {
        ErrorProperties.IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    /**
     * Provide access to the error properties.
     *
     * @return the error properties
     */
    protected ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }
}