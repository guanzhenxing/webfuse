package cn.webfuse.ext.launch.shutdown.tomcat;

import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;


/**
 * 优雅关闭Tomcat配置(see : https://github.com/spring-projects/spring-boot/issues/4657#issuecomment-161354811)
 */
@Configuration
@ConditionalOnClass({Servlet.class, Tomcat.class})
public class TomcatGracefulShutdownConfig {

    @Bean
    public TomcatGracefulShutdown tomcatGracefulShutdown() {
        return new TomcatGracefulShutdown();
    }


    @Bean
    public ServletWebServerFactory servletWebServerFactory(final TomcatGracefulShutdown tomcatGracefulShutdown) {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(tomcatGracefulShutdown);
        return factory;
    }


}
