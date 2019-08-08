package cn.webfuse.framework.monitor.impl;

import cn.webfuse.framework.monitor.MonitorSupport;
import cn.webfuse.framework.monitor.MonitorWriter;

import java.util.List;

public class AccessMonitorSupport extends MonitorSupport {


    public AccessMonitorSupport(List<MonitorWriter> monitorWriterList) {
        super(monitorWriterList);
    }


}
