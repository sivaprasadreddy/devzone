package com.sivalabs.devzone.config;

import com.sivalabs.devzone.domain.services.LinkService;
import com.sivalabs.devzone.domain.services.LinksImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
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

    @Override
    public void run(String... args) throws Exception {
        if (applicationProperties.isImportDataEnabled()) {
            linkService.deleteAllLinks();
            String fileName = applicationProperties.getImportFilePath();
            linksImportService.importLinks(fileName);
        } else {
            log.info("Data importing is disabled");
        }
    }
}
