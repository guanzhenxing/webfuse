package cn.webfuse.framework.constant;

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
@AllArgsConstructor
@Getter
public enum BasicSystemCode {

    /**
     * 系统错误
     */
    SYSTEM_ERROR(500, "100001"),

    /**
     * 系统无法访问
     */
    SERVICE_UNAVAILABLE(503, "100002"),

    /**
     * 远程服务错误
     */
    REMOTE_SERVICE_ERROR(500, "100003"),

    /**
     * 请求的链接/接口不存在
     */
    REQUEST_URI_NOT_FOUND(404, "100004"),

    /**
     * 参数错误
     */
    PARAMETER_ERROR(400, "100005"),

    /**
     * 权限不足
     */
    PERMISSION_DENIED(403, "100006"),

    /**
     * 请求的方法不对
     */
    REQUEST_METHOD_NOT_ALLOWED(405, "100007"),

    /**
     * 资源内容无法匹配
     */
    RESOURCE_NOT_ACCEPTABLE(406, "100008"),

    /**
     * 资源冲突
     */
    RESOURCE_CONFLICT(409, "100009"),

    /**
     * 不支持的mediaType
     */
    UNSUPPORTED_MEDIA_TYPE(415, "100010"),


    ;


    private Integer status;
    private String code;

    //系统级错误代码


    //服务级错误代码


}
