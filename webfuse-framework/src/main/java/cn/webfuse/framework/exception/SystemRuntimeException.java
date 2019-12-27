package cn.webfuse.framework.exception;

import cn.webfuse.core.constant.BaseErrorCode;
import cn.webfuse.core.exception.ErrorCode;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统运行时异常
 *
 * @author Jesen
 */
public class SystemRuntimeException extends BaseWebfuseException {

    public SystemRuntimeException(ErrorCode errorCode, Map<String, Object> extra) {
        super(errorCode, extra);
    }

    public SystemRuntimeException(ErrorCode errorCode, Map<String, Object> extra, Throwable cause) {
        super(errorCode, extra, cause);
    }

    public SystemRuntimeException(Throwable cause) {
        super(cause);
    }

    public SystemRuntimeException() {
        this(BaseErrorCode.SYSTEM_ERROR, new HashMap<>());
    }

    public SystemRuntimeException(Map<String, Object> extra) {
        this(BaseErrorCode.SYSTEM_ERROR, extra);
    }


}
