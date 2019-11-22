package cn.webfuse.ratelimiter.exception;

import cn.webfuse.core.constant.BaseErrorCode;
import cn.webfuse.core.exception.AbstractWebfuseException;
import cn.webfuse.core.exception.ErrorCode;
import org.apache.commons.collections.map.SingletonMap;

import java.util.Map;

public class RateLimiterException extends AbstractWebfuseException {

    public RateLimiterException(String extraMsg) {
        this(BaseErrorCode.SERVICE_UNAVAILABLE, new SingletonMap("description", extraMsg));
    }

    public RateLimiterException(Map<String, Object> extra) {
        this(BaseErrorCode.SERVICE_UNAVAILABLE, extra);
    }

    public RateLimiterException(ErrorCode errorCode, Map<String, Object> extra) {
        super(errorCode, extra);
    }

    public RateLimiterException(ErrorCode errorCode, Map<String, Object> extra, Throwable cause) {
        super(errorCode, extra, cause);
    }
}
