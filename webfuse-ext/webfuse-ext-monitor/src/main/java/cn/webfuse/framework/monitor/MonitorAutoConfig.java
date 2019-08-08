package cn.webfuse.framework.monitor;

import cn.webfuse.framework.monitor.impl.AccessMonitorSupport;
import cn.webfuse.framework.monitor.writer.LoggerWriter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
public abstract class MonitorAutoConfig {


    private List<MonitorWriter> monitorWriterList;

    @PostConstruct
    private void postConstruct() {
        monitorWriterList = new ArrayList<>();
        monitorWriterList.addAll(buildMonitorWriterList());
        if (CollectionUtils.isEmpty(monitorWriterList)) {
            monitorWriterList.add(loggerWriter());
        }
    }


    @Bean
    public AccessMonitorSupport accessMonitorSupport() {
        return new AccessMonitorSupport(monitorWriterList);
    }


    @Bean
    public LoggerWriter loggerWriter() {
        return new LoggerWriter();
    }


    protected abstract Collection<? extends MonitorWriter> buildMonitorWriterList();


}
