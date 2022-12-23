package com.sivalabs.devzone.posts.usecases.createpost;

import com.sivalabs.devzone.posts.domain.models.Category;
import com.sivalabs.devzone.posts.domain.models.CreatePostRequest;
import com.sivalabs.devzone.posts.domain.models.Post;
import com.sivalabs.devzone.posts.domain.utils.JsoupUtils;
import com.sivalabs.devzone.posts.gateways.data.repository.PostRepository;
import com.sivalabs.devzone.users.domain.User;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreatePostHandler {
    private static final Logger logger = LoggerFactory.getLogger(CreatePostHandler.class);

    private final PostRepository postRepository;

    public CreatePostHandler(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(CreatePostRequest createPostRequest) {
        logger.debug("process=create_post, url={}", createPostRequest.url());
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
