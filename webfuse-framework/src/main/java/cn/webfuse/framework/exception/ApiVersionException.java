package cn.webfuse.framework.exception;


import cn.webfuse.core.exception.ErrorCode;

import java.util.Map;

public class ApiVersionException extends BaseWebfuseException {

    public ApiVersionException(ErrorCode errorCode, Map<String, Object> extra) {
        super(errorCode, extra);
    }

    public ApiVersionException(ErrorCode errorCode, Map<String, Object> extra, Throwable cause) {
        super(errorCode, extra, cause);
    }

    public ApiVersionException(Throwable cause) {
        super(cause);
    }
}
