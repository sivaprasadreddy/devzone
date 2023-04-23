package com.sivalabs.devzone.posts.application.usecases.getposts;

import com.sivalabs.devzone.common.model.PagedResult;
import com.sivalabs.devzone.posts.application.data.repository.CategoryRepository;
import com.sivalabs.devzone.posts.application.data.repository.PostRepository;
import com.sivalabs.devzone.posts.domain.model.Category;
import com.sivalabs.devzone.posts.domain.model.Post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GetPostsHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetPostsHandler.class);

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final PostDtoMapper postDtoMapper;

    public GetPostsHandler(
            PostRepository postRepository,
            CategoryRepository categoryRepository,
            PostDtoMapper postDtoMapper) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.postDtoMapper = postDtoMapper;
    }

    @Transactional(readOnly = true)
    public PagedResult<PostDTO> getPosts(Integer page) {
        logger.debug("process=get_posts, page={}", page);
        return convert(postRepository.getAllPosts(page));
    }

    @Transactional(readOnly = true)
    public PagedResult<PostDTO> searchPosts(String query, Integer page) {
        logger.debug("process=search_posts, query={}, page={}", query, page);
        return convert(postRepository.searchPosts(query, page));
    }

    @Transactional(readOnly = true)
    public PagedResult<PostDTO> getPostsByCategory(String category, Integer page) {
        logger.debug("process=get_posts_by_category, category={}, page={}", category, page);
        return convert(postRepository.getPostsByCategory(category, page));
    }

    private PagedResult<PostDTO> convert(PagedResult<Post> postsPage) {
        List<PostDTO> postDTOs = postDtoMapper.toDTOs(postsPage.getData());
        return new PagedResult<>(
                postDTOs,
                postsPage.getTotalElements(),
                postsPage.getPageNumber(),
                postsPage.getTotalPages(),
                postsPage.isFirst(),
                postsPage.isLast(),
                postsPage.isHasNext(),
                postsPage.isHasPrevious());
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
