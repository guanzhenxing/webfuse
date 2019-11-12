package cn.webfuse.framework.exception;


import cn.webfuse.core.exception.ErrorCode;

import java.util.Map;

public class ApiVersionException extends BaseWebfuseException {

    public ApiVersionException(ErrorCode errorCode, Map<String, Object> data) {
        super(errorCode, data);
    }

    public ApiVersionException(ErrorCode errorCode, Map<String, Object> data, Throwable cause) {
        super(errorCode, data, cause);
    }

    public ApiVersionException(Throwable cause) {
        super(cause);
    }
}
