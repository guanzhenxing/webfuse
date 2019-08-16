package cn.webfuse.framework.exception;

import cn.webfuse.core.exception.AbstractWebfuseException;
import cn.webfuse.core.exception.ErrorCode;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;

public class BaseWebfuseException extends AbstractWebfuseException {

    public BaseWebfuseException(ErrorCode errorCode, Map<String, Object> data) {
        super(errorCode, data);
    }

    public BaseWebfuseException(ErrorCode errorCode, Map<String, Object> data, Throwable cause) {
        super(errorCode, data, cause);
    }

    public BaseWebfuseException(Throwable cause) {
        this(BaseErrorCode.SYSTEM_ERROR, of("detail", cause.getMessage()), cause);
    }

}
