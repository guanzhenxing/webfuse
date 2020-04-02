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
    private final Map<String, Object> extra = new HashMap<>();

    public AbstractWebfuseException(ErrorCode errorCode, Map<String, Object> extra) {
        super(format(errorCode.getCode(), errorCode.getMessage(), extra));
        this.errorCode = errorCode;
        if (!MapUtils.isEmpty(extra)) {
            this.extra.putAll(extra);
        }
    }

    public AbstractWebfuseException(ErrorCode errorCode, Map<String, Object> extra, Throwable cause) {
        super(format(errorCode.getCode(), errorCode.getMessage(), extra), cause);
        this.errorCode = errorCode;
        if (!MapUtils.isEmpty(extra)) {
            this.extra.putAll(extra);
        }
    }


    private static String format(String code, String message, Map<String, Object> extra) {
        return String.format("[%s]%s:%s.", code, message, MapUtils.isEmpty(extra) ? "" : extra.toString());
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}

