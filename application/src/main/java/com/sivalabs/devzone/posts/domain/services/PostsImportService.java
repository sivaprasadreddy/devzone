package com.sivalabs.devzone.posts.domain.services;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.posts.domain.models.CreatePostRequest;
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
public class PostsImportService {
    public static final Long SYSTEM_USER_ID = 1L;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PostsImportService.class);
    private final PostService postService;

    public PostsImportService(PostService postService) {
        this.postService = postService;
    }

    @Async
    public void importPostsAsync(List<String> fileNames) throws Exception {
        postService.deleteAllPosts();
        for (String fileName : fileNames) {
            log.info("Importing posts from file: {}", fileName);
            ClassPathResource file = new ClassPathResource(fileName, this.getClass());
            long count = this.importPosts(file.getInputStream());
            log.info("Imported {} posts from file {}", count, fileName);
        }
    }

    public long importPosts(InputStream is) throws IOException, CsvValidationException {
        long count = 0L;

        try (InputStreamReader isr = new InputStreamReader(is, UTF_8);
                CSVReader csvReader = new CSVReader(isr)) {
            csvReader.skip(1);
            CSVIterator iterator = new CSVIterator(csvReader);

            while (iterator.hasNext()) {
                String[] postTokens = iterator.next();
                CreatePostRequest createPostRequest = parsePost(postTokens);
                postService.createPost(createPostRequest);
                count++;
            }
        }
        return count;
    }

    private CreatePostRequest parsePost(String[] postTokens) {
        String category = null;
        if (postTokens.length > 2 && StringUtils.trimToNull(postTokens[2]) != null) {
            category = StringUtils.trimToEmpty(postTokens[2].split("\\|")[0]);
        }
        return new CreatePostRequest(postTokens[0], postTokens[1], category, SYSTEM_USER_ID);
    }
}
