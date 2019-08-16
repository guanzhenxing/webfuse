package cn.webfuse.framework.exception;

import cn.webfuse.core.exception.AbstractWebfuseException;

public class BaseWebfuseException extends AbstractWebfuseException {

    /**
     * 状态码
     */
    private int status;

    public BaseWebfuseException(String code, String message, Throwable throwable, String developerMessage, int status) {
        super(code, message, throwable, developerMessage);
        this.status = status;
    }

    public BaseWebfuseException(String code, String message, Throwable throwable, int status) {
        super(code, message, throwable);
        this.status = status;
    }

    public BaseWebfuseException(String code, String message, int status) {
        super(code, message);
        this.status = status;
    }

    public BaseWebfuseException(String message, int status) {
        super(message);
        this.status = status;
    }

    public BaseWebfuseException(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
