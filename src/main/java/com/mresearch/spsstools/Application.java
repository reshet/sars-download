package com.mresearch.spsstools;


import com.mresearch.spsstools.web.SpssDownloadServlet;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import javax.servlet.Servlet;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public EmbeddedServletContainerCustomizer getServletContainerCustomizer() {
      return container -> {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("manifest","text/cache-manifest");
        container.setMimeMappings(mappings);

        ((TomcatEmbeddedServletContainerFactory) container).addConnectorCustomizers(
                connector -> {
                  AbstractHttp11Protocol httpProtocol = (AbstractHttp11Protocol) connector.getProtocolHandler();
                  httpProtocol.setCompression("on");
                  httpProtocol.setCompressionMinSize(256);
                  String mimeTypes = httpProtocol.getCompressableMimeTypes();
                  String mimeTypesWithJson = mimeTypes + "," + MediaType.APPLICATION_JSON_VALUE;
                  httpProtocol.setCompressableMimeTypes(mimeTypesWithJson);
                }
        );
      };
    }

    @Bean
    public Servlet download() {
      return new SpssDownloadServlet();
    }
}
