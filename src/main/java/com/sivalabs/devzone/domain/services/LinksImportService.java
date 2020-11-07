package com.sivalabs.devzone.domain.services;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.domain.models.LinkDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;

import static com.sivalabs.devzone.domain.utils.AppConstants.SYSTEM_USER_ID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LinksImportService {
    private final LinkService linkService;

    public void importLinks(String fileName) throws IOException, CsvValidationException {
        log.info("Importing links from file: {}", fileName);
        long count = 0L;

        ClassPathResource file = new ClassPathResource(fileName, this.getClass());
        try (InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(inputStreamReader);) {
            csvReader.skip(1);
            CSVIterator iterator = new CSVIterator(csvReader);

            while (iterator.hasNext()) {
                String[] nextLine = iterator.next();
                LinkDTO linkDTO = new LinkDTO();
                linkDTO.setUrl(nextLine[0]);
                linkDTO.setTitle(nextLine[1]);
                linkDTO.setCreatedUserId(SYSTEM_USER_ID);
                linkDTO.setCreatedAt(LocalDateTime.now());
                if (nextLine.length > 2 && StringUtils.trimToNull(nextLine[2]) != null) {
                    linkDTO.setTags(Arrays.asList(StringUtils.trimToEmpty(nextLine[2]).split("\\|")));
                }
                linkService.createLink(linkDTO);
                count++;
            }
        }
        log.info("Imported {} links from file {}", count, fileName);
    }
}
