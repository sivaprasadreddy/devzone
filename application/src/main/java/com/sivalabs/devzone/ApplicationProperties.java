package com.sivalabs.devzone;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "devzone")
public record ApplicationProperties(boolean importDataEnabled, List<String> importFilePaths) {}
