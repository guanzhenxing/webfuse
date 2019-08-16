package cn.webfuse.framework.exception.handle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Restful错误
 *
 * @author Jesen
 */
@Getter
@AllArgsConstructor
public class RestfulError {

    /**
     * HttpStatus状态码
     */
    private HttpStatus status;
    /**
     * 异常code码
     */
    private String code;
    /**
     * 异常消息
     */
    private String message;
    /**
     * 异常详情
     */
    private ErrorDetail detail;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorDetail {
        /**
         * requestId
         */
        private String requestId;
        /**
         * 本地的ID，一般为IP地址
         */
        private String hostId;
        /**
         * 服务器时间
         */
        private Date serverTime = new Date();
        /**
         * 开发者消息
         */
        private String developerMessage;
        /**
         * 文档
         */
        private String document;
        /**
         * 额外数据
         */
        private Map<String, Object> data;
        /**
         * 异常
         */
        private Throwable throwable;
        /**
         * 路径
         */
        private String path;
    }


}
