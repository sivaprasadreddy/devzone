package com.sivalabs.devzone.config;

import com.sivalabs.devzone.ApplicationProperties;
import com.sivalabs.devzone.links.domain.services.LinksImportService;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DataInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final ApplicationProperties applicationProperties;
    private final LinksImportService linksImportService;
    private final MessageSource messageSource;

    public DataInitializer(
            ApplicationProperties applicationProperties,
            LinksImportService linksImportService,
            MessageSource messageSource) {
        this.applicationProperties = applicationProperties;
        this.linksImportService = linksImportService;
        this.messageSource = messageSource;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info(
                messageSource.getMessage(
                        "startup-message", null, "Welcome to DevZone!!!", Locale.getDefault()));
        if (applicationProperties.importDataEnabled()) {
            List<String> fileNames = applicationProperties.importFilePaths();
            linksImportService.importLinksAsync(fileNames);
        } else {
            log.info("Data importing is disabled");
        }
    }
}
