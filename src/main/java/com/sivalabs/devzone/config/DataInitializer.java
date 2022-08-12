package com.sivalabs.devzone.config;

import com.sivalabs.devzone.ApplicationProperties;
import com.sivalabs.devzone.domain.services.LinkService;
import com.sivalabs.devzone.domain.services.LinksImportService;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final ApplicationProperties applicationProperties;
    private final LinkService linkService;
    private final LinksImportService linksImportService;
    private final MessageSource messageSource;

    @Override
    public void run(String... args) throws Exception {
        log.info(
                messageSource.getMessage(
                        "startup-message", null, "Welcome to DevZone!!!", Locale.getDefault()));
        if (applicationProperties.isImportDataEnabled()) {
            linkService.deleteAllLinks();
            String fileName = applicationProperties.getImportFilePath();
            log.info("Importing links from file: {}", fileName);
            ClassPathResource file = new ClassPathResource(fileName, this.getClass());
            long count = linksImportService.importLinks(file.getInputStream());
            log.info("Imported {} links from file {}", count, fileName);
        } else {
            log.info("Data importing is disabled");
        }
    }
}
