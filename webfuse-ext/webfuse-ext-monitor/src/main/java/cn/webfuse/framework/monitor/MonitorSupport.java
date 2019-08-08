package cn.webfuse.framework.monitor;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class MonitorSupport {

    List<MonitorWriter> monitorWriterList;

    @Autowired
    public MonitorSupport(List<MonitorWriter> monitorWriterList) {
        this.monitorWriterList = monitorWriterList;
    }

    protected void write(MonitorInfo monitorInfo) {
        this.monitorWriterList.parallelStream().forEach(monitorWriter -> monitorWriter.write(monitorInfo));
    }


}
