package cn.webfuse.core.exception;

import lombok.Getter;
import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽象的业务异常类
 *
 * @author Jesen
 */
@Getter
public abstract class AbstractWebfuseException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> data = new HashMap<>();

    public AbstractWebfuseException(ErrorCode errorCode, Map<String, Object> data) {
        super(format(errorCode.getCode(), errorCode.getMessage(), data));
        this.errorCode = errorCode;
        if (!MapUtils.isEmpty(data)) {
            this.data.putAll(data);
        }
    }

    public AbstractWebfuseException(ErrorCode errorCode, Map<String, Object> data, Throwable cause) {
        super(format(errorCode.getCode(), errorCode.getMessage(), data), cause);
        this.errorCode = errorCode;
        if (!MapUtils.isEmpty(data)) {
            this.data.putAll(data);
        }
    }


    private static String format(String code, String message, Map<String, Object> data) {
        return String.format("[%s]%s:%s.", code, message, MapUtils.isEmpty(data) ? "" : data.toString());
    }
}

