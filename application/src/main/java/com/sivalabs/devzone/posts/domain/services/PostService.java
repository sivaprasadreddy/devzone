package com.sivalabs.devzone.posts.domain.services;

import static com.sivalabs.devzone.posts.domain.utils.StringUtils.toSlug;

import com.sivalabs.devzone.common.PagedResult;
import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.posts.domain.models.Category;
import com.sivalabs.devzone.posts.domain.models.CreatePostRequest;
import com.sivalabs.devzone.posts.domain.models.Post;
import com.sivalabs.devzone.posts.domain.models.UpdatePostRequest;
import com.sivalabs.devzone.posts.domain.repositories.CategoryRepository;
import com.sivalabs.devzone.posts.domain.repositories.PostRepository;
import com.sivalabs.devzone.posts.domain.utils.JsoupUtils;
import com.sivalabs.devzone.users.domain.models.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PostService.class);
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    public PostService(CategoryRepository categoryRepository, PostRepository postRepository) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public PagedResult<Post> getPosts(Integer page) {
        log.debug("process=get_posts, page={}", page);
        return postRepository.getAllPosts(page);
    }

    @Transactional(readOnly = true)
    public PagedResult<Post> searchPosts(String query, Integer page) {
        log.debug("process=search_posts, query={}, page={}", query, page);
        return postRepository.searchPosts(query, page);
    }

    @Transactional(readOnly = true)
    public PagedResult<Post> getPostsByCategory(String category, Integer page) {
        log.debug("process=get_posts_by_category, category={}, page={}", category, page);
        return postRepository.getPostsByCategory(category, page);
    }

    @Transactional(readOnly = true)
    public Optional<Post> getPostById(Long id) {
        log.debug("process=get_post_by_id, id={}", id);
        return postRepository.findById(id);
    }

    public Post createPost(CreatePostRequest createPostRequest) {
        log.debug("process=create_post, url={}", createPostRequest.url());
        Category category = this.buildCategory(createPostRequest.category());
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

    public Post updatePost(UpdatePostRequest updatePostRequest) {
        log.debug("process=update_post, id={}", updatePostRequest.id());
        Post post = postRepository.findById(updatePostRequest.id()).orElse(null);
        if (post == null) {
            throw new ResourceNotFoundException(
                    "Post with id: " + updatePostRequest.id() + " not found");
        }
        Category category = this.buildCategory(updatePostRequest.category());
        Post updatedPost =
                new Post(
                        post.id(),
                        updatePostRequest.url(),
                        getTitle(updatePostRequest.url(), updatePostRequest.title()),
                        category,
                        post.createdBy(),
                        post.createdAt(),
                        post.updatedAt());
        return postRepository.save(updatedPost);
    }

    public void deletePost(Long id) {
        log.debug("process=delete_post_by_id, id={}", id);
        postRepository.deleteById(id);
    }

    public void deleteAllPosts() {
        log.debug("process=delete_all_posts");
        postRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    private String getTitle(String url, String title) {
        if (StringUtils.isNotEmpty(title)) {
            return title;
        }
        return JsoupUtils.getTitle(url);
    }

    private Category buildCategory(String categoryName) {
        if (StringUtils.isBlank(categoryName)) {
            return null;
        }
        String name = toSlug(categoryName.trim());
        return new Category(null, name);
    }
}
