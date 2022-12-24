package com.sivalabs.devzone.posts.usecases.uploadposts;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.posts.domain.models.Category;
import com.sivalabs.devzone.posts.domain.models.Post;
import com.sivalabs.devzone.posts.domain.utils.JsoupUtils;
import com.sivalabs.devzone.posts.gateways.data.repository.PostRepository;
import com.sivalabs.devzone.users.domain.User;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UploadPostsHandler {
    public static final Long SYSTEM_USER_ID = 1L;
    private static final Logger log = LoggerFactory.getLogger(UploadPostsHandler.class);

    private final PostRepository postRepository;

    public UploadPostsHandler(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Async
    public void importPostsAsync(List<String> fileNames)
            throws IOException, CsvValidationException {
        postRepository.deleteAll();
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
                Post post = parsePost(postTokens);
                postRepository.save(post);
                count++;
            }
        }
        return count;
    }

    private Post parsePost(String[] postTokens) {
        String categoryName = null;
        if (postTokens.length > 2 && StringUtils.trimToNull(postTokens[2]) != null) {
            categoryName = StringUtils.trimToEmpty(postTokens[2].split("\\|")[0]);
        }
        Category category = categoryName == null ? null : Category.buildCategory(categoryName);
        String url = postTokens[0];
        String title = postTokens[1];
        if (StringUtils.isEmpty(title)) {
            title = JsoupUtils.getTitle(url);
        }
        User user = new User(SYSTEM_USER_ID);
        return new Post(null, url, title, category, user, LocalDateTime.now(), null);
    }
}
