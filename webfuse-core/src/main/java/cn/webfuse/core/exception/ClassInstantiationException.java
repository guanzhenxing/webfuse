package cn.webfuse.core.exception;

public class ClassInstantiationException extends RuntimeException {

    public ClassInstantiationException() {
    }

    public ClassInstantiationException(String message) {
        super(message);
    }

    public ClassInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassInstantiationException(Throwable cause) {
        super(cause);
    }

    public ClassInstantiationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
