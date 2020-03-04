package cn.webfuse.core.constant;

import cn.webfuse.core.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 基础的错误代码
 * <p>
 * <p>
 * 参考：
 * <li>https://open.weibo.com/wiki/Error_code</li>
 * <li>http://www.ruanyifeng.com/blog/2018/10/restful-api-best-practices.html</li>
 * <li>https://mp.weixin.qq.com/s/gqCAUvIdzj1D2-IpQS4LvA</li>
 * <p>
 * https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status
 *
 * @author Jesen
 */
@Data
@AllArgsConstructor
public class BaseErrorCode implements ErrorCode {

    private int httpStatus;
    private String message;
    private String code;


    protected static BaseErrorCode build(int httpStatus, String code, String message) {
        return new BaseErrorCode(httpStatus, code, message);
    }


    /**
     * 请求的链接/接口不存在
     */
    public static BaseErrorCode REQUEST_URI_NOT_FOUND = build(404, "404", "REQUEST_URI_NOT_FOUND");

    /**
     * 服务器不理解客户端的请求，未做任何处理。即：参数错误
     */
    public static BaseErrorCode BAD_REQUEST = build(400, "400", "BAD_REQUEST");

    /**
     * 请求的方法不对
     */
    public static BaseErrorCode REQUEST_METHOD_NOT_ALLOWED = build(405, "405", "REQUEST_METHOD_NOT_ALLOWED");

    /**
     * 资源内容无法匹配
     */
    public static BaseErrorCode RESOURCE_NOT_ACCEPTABLE = build(406, "406", "RESOURCE_NOT_ACCEPTABLE");

    /**
     * 资源冲突
     */
    public static BaseErrorCode RESOURCE_CONFLICT = build(409, "409", "RESOURCE_CONFLICT");

    /**
     * 不支持的mediaType
     */
    public static BaseErrorCode UNSUPPORTED_MEDIA_TYPE = build(415, "415", "UNSUPPORTED_MEDIA_TYPE");


    /**
     * 客户端请求有效，服务器处理时发生了意外。即：系统错误
     */
    public static BaseErrorCode SYSTEM_ERROR = build(500, "100000", "SYSTEM_ERROR");


    /**
     * 服务器无法处理请求，一般用于网站维护状态
     */
    public static BaseErrorCode SERVICE_UNAVAILABLE = build(503, "100001", "SERVICE_UNAVAILABLE");


}
