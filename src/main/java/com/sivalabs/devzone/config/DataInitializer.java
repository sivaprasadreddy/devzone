package com.sivalabs.devzone.config;

import com.sivalabs.devzone.ApplicationProperties;
import com.sivalabs.devzone.domain.services.LinkService;
import com.sivalabs.devzone.domain.services.LinksImportService;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.MessageSource;
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
            List<String> fileNames = applicationProperties.getImportFilePaths();
            linksImportService.importLinksAsync(fileNames);
        } else {
            log.info("Data importing is disabled");
        }
    }
}
