package cn.webfuse.ext.launch.shutdown.undertow;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;

@Configuration
@ConditionalOnClass({Servlet.class, Undertow.class})
public class UndertowGracefulShutdownConfig {

    @Bean
    public UndertowGracefulShutdown undertowGracefulShutdown() {
        return new UndertowGracefulShutdown();
    }

    @Bean
    public UndertowGracefulShutdownWrapper gracefulShutdownWrapper() {
        return new UndertowGracefulShutdownWrapper();
    }

    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
        factory.addDeploymentInfoCustomizers(deploymentInfo -> deploymentInfo.addOuterHandlerChainWrapper(gracefulShutdownWrapper()));
        factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ENABLE_STATISTICS, true));
        return factory;
    }

}
