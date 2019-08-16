package cn.webfuse.framework.exception;

import cn.webfuse.core.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基础的错误代码
 * <p>
 * <p>
 * 参考：
 * <li>https://open.weibo.com/wiki/Error_code</li>
 * <li>http://www.ruanyifeng.com/blog/2018/10/restful-api-best-practices.html</li>
 * <p>
 * https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status
 *
 * @author Jesen
 */
public enum BaseErrorCode implements ErrorCode {

    /**
     * 系统错误
     */
    SYSTEM_ERROR(500, "SYSTEM_ERROR"),

    /**
     * 系统无法访问
     */
    SERVICE_UNAVAILABLE(503, "SERVICE_UNAVAILABLE"),

    /**
     * 远程服务错误
     */
    REMOTE_SERVICE_ERROR(500, "REMOTE_SERVICE_ERROR"),

    /**
     * 请求的链接/接口不存在
     */
    REQUEST_URI_NOT_FOUND(404, "REQUEST_URI_NOT_FOUND"),

    /**
     * 参数错误
     */
    PARAMETER_ERROR(400, "PARAMETER_ERROR"),

    /**
     * 权限不足
     */
    PERMISSION_DENIED(403, "PERMISSION_DENIED"),

    /**
     * 请求的方法不对
     */
    REQUEST_METHOD_NOT_ALLOWED(405, "REQUEST_METHOD_NOT_ALLOWED"),

    /**
     * 资源内容无法匹配
     */
    RESOURCE_NOT_ACCEPTABLE(406, "RESOURCE_NOT_ACCEPTABLE"),

    /**
     * 资源冲突
     */
    RESOURCE_CONFLICT(409, "RESOURCE_CONFLICT"),

    /**
     * 不支持的mediaType
     */
    UNSUPPORTED_MEDIA_TYPE(415, "UNSUPPORTED_MEDIA_TYPE"),


    ;

    private int status;
    private String message;

    BaseErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return this.name();
    }

}
