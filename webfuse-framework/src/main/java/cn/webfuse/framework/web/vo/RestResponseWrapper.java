package cn.webfuse.framework.web.vo;

import cn.webfuse.framework.exception.handle.RestfulError;
import cn.webfuse.framework.kit.HttpServletKits;
import cn.webfuse.framework.kit.LocalHostInfoKits;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * Restful返回值的封装。继承了ResponseEntity。
 *
 * @param <T> 返回的实体对象
 * @author Jesen
 */
public class RestResponseWrapper<T> extends ResponseEntity<T> {

    public RestResponseWrapper(HttpStatus status) {
        super(status);
    }

    public RestResponseWrapper(T body, HttpStatus status) {
        super(body, status);
    }

    public RestResponseWrapper(MultiValueMap<String, String> headers, HttpStatus status) {
        super(headers, status);
    }

    public RestResponseWrapper(T body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }

    public static <T> RestResponseWrapper<T> createdResponse(T body) {
        return new RestResponseWrapper(body, HttpStatus.CREATED);
    }

    public static <T> RestResponseWrapper<T> okResponse(T body) {
        return new RestResponseWrapper<>(body, HttpStatus.OK);
    }

    public static <T> RestResponseWrapper<T> noContentResponse() {
        return new RestResponseWrapper<>(HttpStatus.NO_CONTENT);
    }

    public static RestResponseWrapper<RestfulError> badReRequestResponse(String code, String message) {
        return restfulError(HttpStatus.BAD_REQUEST, code, message);
    }

    public static RestResponseWrapper<RestfulError> restfulError(HttpStatus status, String code, String message) {

        RestfulError.ErrorDetail errorDetail = new RestfulError.ErrorDetail();
        errorDetail.setRequestId(HttpServletKits.getRequest().getHeader("X-Request-Id"));
        errorDetail.setHostId(LocalHostInfoKits.getLocalHost());

        RestfulError restfulError = new RestfulError(status, code, message, errorDetail);

        return new RestResponseWrapper<>(restfulError, status);
    }


}
