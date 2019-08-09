package cn.webfuse.core.exception;

/**
 * 抽象的业务异常类
 *
 * @author Jesen
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

    /**
     * 抽象的业务异常
     *
     * @param status           状态码（可做HTTP状态码使用）
     * @param code             异常代码
     * @param message          异常消息
     * @param throwable        异常throwable
     * @param developerMessage 开发者消息
     */
    public AbstractBizException(int status, String code, String message, Throwable throwable, String developerMessage) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.throwable = throwable;
        this.developerMessage = developerMessage;
    }

    /**
     * 抽象的业务异常
     *
     * @param status    状态码（可做HTTP状态码使用）
     * @param code      异常代码
     * @param message   异常消息
     * @param throwable 异常throwable
     */
    public AbstractBizException(int status, String code, String message, Throwable throwable) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.throwable = throwable;
    }

    /**
     * 抽象的业务异常
     *
     * @param status  状态码（可做HTTP状态码使用）
     * @param code    异常代码
     * @param message 异常消息
     */
    public AbstractBizException(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    /**
     * 抽象的业务异常
     *
     * @param code    异常代码
     * @param message 异常消息
     */
    public AbstractBizException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 抽象的业务异常
     *
     * @param message 异常消息
     */
    public AbstractBizException(String message) {
        super(message);
    }

    /**
     * 抽象的业务异常
     */
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

