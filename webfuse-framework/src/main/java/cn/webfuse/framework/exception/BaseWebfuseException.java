package cn.webfuse.framework.exception;

import cn.webfuse.core.exception.AbstractWebfuseException;
import cn.webfuse.core.constant.BaseErrorCode;
import cn.webfuse.core.exception.ErrorCode;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;

public class BaseWebfuseException extends AbstractWebfuseException {

    public BaseWebfuseException(ErrorCode errorCode, Map<String, Object> extra) {
        super(errorCode, extra);
    }

    public BaseWebfuseException(ErrorCode errorCode, Map<String, Object> extra, Throwable cause) {
        super(errorCode, extra, cause);
    }

    public BaseWebfuseException(Throwable cause) {
        this(BaseErrorCode.SYSTEM_ERROR, of("detail", cause.getMessage()), cause);
    }

}
