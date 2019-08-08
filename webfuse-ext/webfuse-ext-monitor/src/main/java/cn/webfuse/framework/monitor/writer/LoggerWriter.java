package cn.webfuse.framework.monitor.writer;

import cn.webfuse.framework.monitor.MonitorInfo;
import cn.webfuse.framework.monitor.MonitorWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerWriter implements MonitorWriter {

    @Override
    public void write(MonitorInfo monitorInfo) {
        Logger logger = LoggerFactory.getLogger("MONITOR");
        logger.info(monitorInfo.toString());
    }

}
