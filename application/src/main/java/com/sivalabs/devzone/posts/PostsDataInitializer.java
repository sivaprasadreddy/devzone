package com.sivalabs.devzone.posts;

import com.sivalabs.devzone.ApplicationProperties;
import com.sivalabs.devzone.posts.usecases.uploadposts.UploadPostsHandler;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class PostsDataInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(PostsDataInitializer.class);

    private final ApplicationProperties applicationProperties;
    private final UploadPostsHandler uploadPostsHandler;

    public PostsDataInitializer(
            ApplicationProperties applicationProperties, UploadPostsHandler uploadPostsHandler) {
        this.applicationProperties = applicationProperties;
        this.uploadPostsHandler = uploadPostsHandler;
    }

    @Override
    public void run(String... args) throws Exception {
        if (applicationProperties.importDataEnabled()) {
            List<String> fileNames = applicationProperties.importFilePaths();
            uploadPostsHandler.importPostsAsync(fileNames);
        } else {
            log.info("Data importing is disabled");
        }
    }
}
