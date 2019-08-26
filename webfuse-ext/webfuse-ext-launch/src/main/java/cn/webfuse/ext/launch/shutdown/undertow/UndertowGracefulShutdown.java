package cn.webfuse.ext.launch.shutdown.undertow;

import io.undertow.Undertow;
import io.undertow.server.ConnectorStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 优雅关闭undertow (see : https://github.com/spring-projects/spring-boot/issues/4657)
 */
@Slf4j
public class UndertowGracefulShutdown implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    private UndertowGracefulShutdownWrapper gracefulShutdownWrapper;

    @Autowired
    private ServletWebServerApplicationContext context;

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        gracefulShutdownWrapper.getGracefulShutdownHandler().shutdown();
        try {
            UndertowServletWebServer webServer = (UndertowServletWebServer) context.getWebServer();
            Field field = webServer.getClass().getDeclaredField("undertow");
            field.setAccessible(true);
            Undertow undertow = (Undertow) field.get(webServer);
            List<Undertow.ListenerInfo> listenerInfo = undertow.getListenerInfo();
            Undertow.ListenerInfo listener = listenerInfo.get(0);
            ConnectorStatistics connectorStatistics = listener.getConnectorStatistics();
            while (connectorStatistics.getActiveConnections() > 0) {
            }
        } catch (Exception e) {
            // Application Shutdown
        }
    }
}

