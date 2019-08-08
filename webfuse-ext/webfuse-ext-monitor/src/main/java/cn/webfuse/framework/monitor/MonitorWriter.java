package cn.webfuse.framework.monitor;

public interface MonitorWriter {

    /**
     * 当产生访问日志时,将调用此方法.注意,此方法内的操作应尽量设置为异步操作,否则可能影响请求性能
     *
     * @param monitorInfo 产生的监控信息
     */
    void write(MonitorInfo monitorInfo);
    
}
