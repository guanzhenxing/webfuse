package cn.webfuse.framework.exception.handle.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * RestfulError的返回信息
 *
 * @author Jesen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultRestfulErrorVO {

    /**
     * 错误的代码
     */
    private String code;
    /**
     * 错误的信息
     */
    private String message;
    /**
     * 开发者信息
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String developerMessage;

    /**
     * 发生错误的服务器时间
     */
    private Date serverTime;

    /**
     * 请求的ID(如果有)
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String requestId;

    /**
     * 服务器实例ID(如果有)
     */
    private String hostId;

    /**
     * 错误解决的文档(如果有)
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String document;


}
