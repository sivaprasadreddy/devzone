package com.sivalabs.devzone.posts.usecases.updatepost;

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.posts.domain.models.Category;
import com.sivalabs.devzone.posts.domain.models.Post;
import com.sivalabs.devzone.posts.gateways.data.repository.PostRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdatePostHandler {
    private static final Logger logger = LoggerFactory.getLogger(UpdatePostHandler.class);

    private final PostRepository postRepository;

    public UpdatePostHandler(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Post> getPostById(Long id) {
        logger.debug("process=get_post_by_id, id={}", id);
        return postRepository.findById(id);
    }

    public Post updatePost(UpdatePostRequest updatePostRequest) {
        logger.debug("process=update_post, id={}", updatePostRequest.id());
        Post post = postRepository.findById(updatePostRequest.id()).orElse(null);
        if (post == null) {
            throw new ResourceNotFoundException(
                    "Post with id: " + updatePostRequest.id() + " not found");
        }
        Category category = Category.buildCategory(updatePostRequest.category());
        Post updatedPost =
                new Post(
                        post.id(),
                        updatePostRequest.url(),
                        updatePostRequest.derivedTitle(),
                        category,
                        post.createdBy(),
                        post.createdAt(),
                        post.updatedAt());
        return postRepository.save(updatedPost);
    }
}
