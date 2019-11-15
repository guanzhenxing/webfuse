package cn.webfuse.ratelimiter.exception;

import cn.webfuse.core.constant.BaseErrorCode;
import cn.webfuse.core.exception.AbstractWebfuseException;
import cn.webfuse.core.exception.ErrorCode;
import org.apache.commons.collections.map.SingletonMap;

import java.util.Map;

public class RateLimiterException extends AbstractWebfuseException {

    public RateLimiterException(String data) {
        this(BaseErrorCode.SERVICE_UNAVAILABLE, new SingletonMap("description", data));
    }

    public RateLimiterException(Map<String, Object> data) {
        this(BaseErrorCode.SERVICE_UNAVAILABLE, data);
    }

    public RateLimiterException(ErrorCode errorCode, Map<String, Object> data) {
        super(errorCode, data);
    }

    public RateLimiterException(ErrorCode errorCode, Map<String, Object> data, Throwable cause) {
        super(errorCode, data, cause);
    }
}
