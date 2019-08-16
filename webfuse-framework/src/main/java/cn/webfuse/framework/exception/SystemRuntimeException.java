package cn.webfuse.framework.exception;

/**
 * 系统运行时异常
 *
 * @author Jesen
 */
public class SystemRuntimeException extends BaseWebfuseException {

    public SystemRuntimeException(String code, String message, Throwable throwable, String developerMessage, int status) {
        super(code, message, throwable, developerMessage, status);
    }

    public SystemRuntimeException(String code, String message, Throwable throwable, int status) {
        super(code, message, throwable, status);
    }

    public SystemRuntimeException(String code, String message, int status) {
        super(code, message, status);
    }

    public SystemRuntimeException(String message, int status) {
        super(message, status);
    }

    public SystemRuntimeException(int status) {
        super(status);
    }
}
