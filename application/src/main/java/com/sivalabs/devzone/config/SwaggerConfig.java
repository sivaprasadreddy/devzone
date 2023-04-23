package com.sivalabs.devzone.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(info());
    }

    private Info info() {
        return new Info()
                .title("DevZone")
                .description("DevZone API")
                .version("0.0.1")
                .contact(
                        new Contact()
                                .email("sivalabs@sivalabs.in")
                                .url("https://sivalabs.in")
                                .name("SivaLabs"));
    }
}
