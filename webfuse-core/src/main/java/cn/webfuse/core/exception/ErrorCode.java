package cn.webfuse.core.exception;

/**
 * 基础的错误代码接口。具体的代码可以实现该接口
 *
 * @author Jesen
 */
public interface ErrorCode {

    /**
     * 状态码
     *
     * @return
     */
    default int getStatus() {
        return 500;
    }

    /**
     * 用户显示给用户的信息。可以作为配置国际化的依据
     *
     * @return
     */
    default String getMessage() {
        return null;
    }

    /**
     * 错误代码。系统内部使用的
     *
     * @return
     */
    default String getCode() {
        return "SYSTEM_ERROR";
    }
}
