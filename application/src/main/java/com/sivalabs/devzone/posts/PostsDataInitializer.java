package com.sivalabs.devzone.posts;

import com.sivalabs.devzone.ApplicationProperties;
import com.sivalabs.devzone.posts.application.usecases.uploadposts.UploadPostsHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class PostsDataInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(PostsDataInitializer.class);

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
            logger.info("Data importing is disabled");
        }
    }
}
