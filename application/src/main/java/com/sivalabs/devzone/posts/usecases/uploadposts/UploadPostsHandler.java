package com.sivalabs.devzone.posts.usecases.uploadposts;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.posts.domain.models.Category;
import com.sivalabs.devzone.posts.domain.models.CreatePostRequest;
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
    public void importPostsAsync(List<String> fileNames) throws Exception {
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
                CreatePostRequest createPostRequest = parsePost(postTokens);
                this.createPost(createPostRequest);
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

    private Post createPost(CreatePostRequest createPostRequest) {
        log.debug("process=create_post, url={}", createPostRequest.url());
        Category category = Category.buildCategory(createPostRequest.category());
        User user = new User(createPostRequest.createdUserId());
        Post post =
                new Post(
                        null,
                        createPostRequest.url(),
                        getTitle(createPostRequest.url(), createPostRequest.title()),
                        category,
                        user,
                        LocalDateTime.now(),
                        null);
        return postRepository.save(post);
    }

    private String getTitle(String url, String title) {
        if (StringUtils.isNotEmpty(title)) {
            return title;
        }
        return JsoupUtils.getTitle(url);
    }
}
