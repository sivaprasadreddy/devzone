package com.sivalabs.devzone.links.domain.services;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.links.domain.models.CreateLinkRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LinksImportService {
    public static final Long SYSTEM_USER_ID = 1L;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LinksImportService.class);
    private final LinkService linkService;

    public LinksImportService(LinkService linkService) {
        this.linkService = linkService;
    }

    @Async
    public void importLinksAsync(List<String> fileNames) throws Exception {
        linkService.deleteAllLinks();
        for (String fileName : fileNames) {
            log.info("Importing links from file: {}", fileName);
            ClassPathResource file = new ClassPathResource(fileName, this.getClass());
            long count = this.importLinks(file.getInputStream());
            log.info("Imported {} links from file {}", count, fileName);
        }
    }

    public long importLinks(InputStream is) throws IOException, CsvValidationException {
        long count = 0L;

        try (InputStreamReader isr = new InputStreamReader(is, UTF_8);
                CSVReader csvReader = new CSVReader(isr)) {
            csvReader.skip(1);
            CSVIterator iterator = new CSVIterator(csvReader);

            while (iterator.hasNext()) {
                String[] linkTokens = iterator.next();
                CreateLinkRequest createLinkRequest = parseLink(linkTokens);
                linkService.createLink(createLinkRequest);
                count++;
            }
        }
        return count;
    }

    private CreateLinkRequest parseLink(String[] linkTokens) {
        CreateLinkRequest createLinkRequest = new CreateLinkRequest();
        createLinkRequest.setUrl(linkTokens[0]);
        createLinkRequest.setTitle(linkTokens[1]);
        createLinkRequest.setCreatedUserId(SYSTEM_USER_ID);
        if (linkTokens.length > 2 && StringUtils.trimToNull(linkTokens[2]) != null) {
            createLinkRequest.setCategory(StringUtils.trimToEmpty(linkTokens[2].split("\\|")[0]));
        }
        return createLinkRequest;
    }
}
