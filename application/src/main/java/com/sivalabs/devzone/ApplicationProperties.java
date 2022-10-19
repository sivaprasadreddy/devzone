package com.sivalabs.devzone;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "devzone")
public record ApplicationProperties(boolean importDataEnabled, List<String> importFilePaths) {}
