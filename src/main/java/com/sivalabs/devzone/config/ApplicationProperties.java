package com.sivalabs.devzone.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "devzone")
@Data
public class ApplicationProperties {
    private boolean importDataEnabled = true;
    private String importFilePath;
}
