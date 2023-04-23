package com.sivalabs.devzone.posts.application.usecases.createpost;

import com.sivalabs.devzone.posts.application.data.repository.PostRepository;
import com.sivalabs.devzone.posts.domain.model.Category;
import com.sivalabs.devzone.posts.domain.model.Post;
import com.sivalabs.devzone.users.domain.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class CreatePostHandler {
    private static final Logger logger = LoggerFactory.getLogger(CreatePostHandler.class);

    private final PostRepository postRepository;

    public CreatePostHandler(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(CreatePostRequest createPostRequest) {
        String url = createPostRequest.url();
        logger.info("process=create_post, url={}", url);
        Category category = Category.buildCategory(createPostRequest.category());
        User user = new User(createPostRequest.createdUserId());
        String title = createPostRequest.derivedTitle();
        Post post = new Post(null, url, title, category, user, LocalDateTime.now(), null);
        return postRepository.save(post);
    }
}
