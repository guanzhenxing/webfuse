package cn.webfuse.framework.core.exception;

/**
 * 抽象的业务异常类
 */
public abstract class AbstractBizException extends RuntimeException {

    /**
     * 状态码
     */
    private int status;

    /**
     * 错误代码
     */
    private String code;
    /**
     * 错误消息（针对用户）
     */
    private String message;
    /**
     * 异常堆栈
     */
    private Throwable throwable;
    /**
     * 给开发者的错误消息
     */
    private String developerMessage;

    public AbstractBizException(int status, String code, String message, Throwable throwable, String developerMessage) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.throwable = throwable;
        this.developerMessage = developerMessage;
    }

    public AbstractBizException(int status, String code, String message, Throwable throwable) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.throwable = throwable;
    }

    public AbstractBizException(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public AbstractBizException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public AbstractBizException(String message) {
        super(message);
    }

    public AbstractBizException() {
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

}

