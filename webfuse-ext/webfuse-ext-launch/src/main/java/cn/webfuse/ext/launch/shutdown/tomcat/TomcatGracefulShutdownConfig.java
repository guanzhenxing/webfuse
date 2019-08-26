package cn.webfuse.ext.launch.shutdown.tomcat;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingClass(value = {"org.springframework.boot.web.embedded.tomcat.TomcatWebServer"})
public class TomcatGracefulShutdownConfig {

    @Bean
    public TomcatGracefulShutdown tomcatGracefulShutdown() {
        return new TomcatGracefulShutdown();
    }

    @Bean
    public ServletWebServerFactory tomcatCustomizer(TomcatGracefulShutdown tomcatGracefulShutdown) {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers(tomcatGracefulShutdown);
        return tomcat;
    }


}
