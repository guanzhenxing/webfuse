package cn.webfuse.framework.exception;

import cn.webfuse.framework.core.exception.AbstractBizException;

/**
 * 系统运行时异常
 *
 * @author Jesen
 */
public class SystemRuntimeException extends AbstractBizException {

    public SystemRuntimeException(int status, String code, String message, Throwable throwable, String developerMessage) {
        super(status, code, message, throwable, developerMessage);
    }

    public SystemRuntimeException(int status, String code, String message, Throwable throwable) {
        super(status, code, message, throwable);
    }

    public SystemRuntimeException(int status, String code, String message) {
        super(status, code, message);
    }

    public SystemRuntimeException(String code, String message) {
        super(code, message);
    }

    public SystemRuntimeException(String message) {
        super(message);
    }

    public SystemRuntimeException() {
    }
}
