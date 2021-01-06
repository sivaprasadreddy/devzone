package com.sivalabs.devzone.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                // .apis(RequestHandlerSelectors.any())
                // .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.sivalabs.devzone.web.api"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "DevZone Java REST API",
                "DevZone Java REST API using SpringBoot",
                "API TOS",
                "Terms of services",
                new Contact("Team", "www.example.com", "support@example.com"),
                "License of API",
                "API license URL",
                Collections.emptyList());
    }
}
