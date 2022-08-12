package com.sivalabs.devzone.domain.services;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.domain.models.LinkDTO;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LinksImportService {
    public static final Long SYSTEM_USER_ID = 1L;
    private final LinkService linkService;

    public long importLinks(InputStream inputStream) throws IOException, CsvValidationException {
        long count = 0L;

        try (InputStreamReader inputStreamReader =
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                CSVReader csvReader = new CSVReader(inputStreamReader); ) {
            csvReader.skip(1);
            CSVIterator iterator = new CSVIterator(csvReader);

            while (iterator.hasNext()) {
                String[] linkTokens = iterator.next();
                LinkDTO linkDTO = new LinkDTO();
                linkDTO.setUrl(linkTokens[0]);
                linkDTO.setTitle(linkTokens[1]);
                linkDTO.setCreatedUserId(SYSTEM_USER_ID);
                linkDTO.setCreatedAt(LocalDateTime.now());
                if (linkTokens.length > 2 && StringUtils.trimToNull(linkTokens[2]) != null) {
                    linkDTO.setCategory(StringUtils.trimToEmpty(linkTokens[2].split("\\|")[0]));
                }
                linkService.createLink(linkDTO);
                count++;
            }
        }
        return count;
    }
}
