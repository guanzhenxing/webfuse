package cn.webfuse.framework.monitor;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.StringJoiner;

@Data
public class MonitorInfo implements Serializable {

    /**
     * 日志id
     */
    private String id;

    /**
     * 访问的操作
     *
     * @see Monitor#value()
     */
    private String action;

    /**
     * 描述
     *
     * @see Monitor#describe()
     */
    private String describe;

    /**
     * 访问对应的java方法
     */
    private Method method;

    /**
     * 访问对应的java类
     */
    private Class clazz;

    /**
     * 请求的参数,参数为java方法的参数而不是http参数,key为参数名,value为参数值.
     */
    private Map<String, Object> parameters;

    /**
     * 异常信息,请求对应方法抛出的异常
     */
    private Throwable exception;

    @Override
    public String toString() {
        return new StringJoiner(", ")
                .add("id='" + id + "'")
                .add("action='" + action + "'")
                .add("describe='" + describe + "'")
                .add("method=" + method)
                .add("clazz=" + clazz)
                .add("parameters=" + parameters)
                .add("exception=" + exception)
                .toString();
    }
}
