package edu.sdccd.cisc191.server.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfiguration {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> customTomcat() {
        return factory -> {
            Connector connector1 = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
            connector1.setPort(8081);
            factory.addAdditionalTomcatConnectors(connector1);

            Connector connector2 = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
            connector2.setPort(8082);
            factory.addAdditionalTomcatConnectors(connector2);
        };
    }
}

