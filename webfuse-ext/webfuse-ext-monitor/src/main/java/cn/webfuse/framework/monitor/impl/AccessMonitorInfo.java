package cn.webfuse.framework.monitor.impl;

import cn.webfuse.framework.monitor.MonitorInfo;
import lombok.Data;

import java.util.Map;
import java.util.StringJoiner;

@Data
public class AccessMonitorInfo extends MonitorInfo {

    /**
     * 请求者ip地址
     */
    private String ip;

    /**
     * 请求的url地址
     */
    private String url;

    /**
     * http 请求头集合
     */
    private Map<String, String> requestHeaders;

    /**
     * http 请求方法
     */
    private String httpMethod;

    /**
     * 响应结果,方法的返回值
     */
    private Object response;

    /**
     * 请求时间戳
     *
     * @see System#currentTimeMillis()
     */
    private long requestTime;

    /**
     * 响应时间戳
     *
     * @see System#currentTimeMillis()
     */
    private long responseTime;

    @Override
    public String toString() {
        return new StringJoiner(", ")
                .add(super.toString())
                .add("ip='" + ip + "'")
                .add("url='" + url + "'")
                .add("requestHeaders=" + requestHeaders)
                .add("httpMethod='" + httpMethod + "'")
                .add("response=" + response)
                .add("requestTime=" + requestTime)
                .add("responseTime=" + responseTime)
                .toString();
    }
}
