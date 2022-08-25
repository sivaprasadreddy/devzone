package com.sivalabs.devzone.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }
}
