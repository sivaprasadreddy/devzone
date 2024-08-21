package com.sivalabs.devzone.posts.adapter.data.repository;

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.common.model.PagedResult;
import com.sivalabs.devzone.posts.adapter.data.entity.CategoryEntity;
import com.sivalabs.devzone.posts.adapter.data.entity.PostEntity;
import com.sivalabs.devzone.posts.adapter.data.mapper.PostMapper;
import com.sivalabs.devzone.posts.application.data.repository.CategoryRepository;
import com.sivalabs.devzone.posts.application.data.repository.PostRepository;
import com.sivalabs.devzone.posts.domain.model.Category;
import com.sivalabs.devzone.posts.domain.model.Post;
import com.sivalabs.devzone.users.adapter.data.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
class PostRepositoryImpl implements PostRepository {
    private static final Integer PAGE_SIZE = 15;
    private final JpaPostRepository jpaPostRepository;
    private final JpaPostCreatorRepository jpaPostCreatorRepository;
    private final JpaCategoryRepository jpaCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final PostMapper postMapper;

    public PostRepositoryImpl(
            JpaPostRepository jpaPostRepository,
            JpaPostCreatorRepository jpaPostCreatorRepository,
            JpaCategoryRepository jpaCategoryRepository,
            CategoryRepository categoryRepository,
            PostMapper postMapper) {
        this.jpaPostRepository = jpaPostRepository;
        this.jpaPostCreatorRepository = jpaPostCreatorRepository;
        this.jpaCategoryRepository = jpaCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.postMapper = postMapper;
    }

    @Override
    public Optional<Post> findById(Long id) {
        return jpaPostRepository.findById(id).map(postMapper::toModel);
    }

    @Override
    public Post save(Post post) {
        PostEntity entity = postMapper.toEntity(post);
        UserEntity createdBy =
                jpaPostCreatorRepository.findById(post.createdBy().id()).orElseThrow();
        CategoryEntity category = this.getOrCreateCategory(post.category());
        entity.setCreatedBy(createdBy);
        entity.setCategory(category);
        var savedPost = jpaPostRepository.save(entity);
        return postMapper.toModel(savedPost);
    }

    @Override
    public PagedResult<Post> getAllPosts(Integer page) {
        Pageable pageable = getPageable(page);
        Page<PostEntity> postsPage = jpaPostRepository.findPosts(pageable);
        return getPosts(pageable, postsPage);
    }

    @Override
    public PagedResult<Post> searchPosts(String query, Integer page) {
        Pageable pageable = getPageable(page);
        Page<PostEntity> postsPage =
                jpaPostRepository.findPostsByTitleContainingIgnoreCase(query, pageable);
        return getPosts(pageable, postsPage);
    }

    @Override
    public PagedResult<Post> getPostsByCategory(String category, Integer page) {
        Optional<CategoryEntity> categoryOptional = jpaCategoryRepository.findByName(category);
        if (categoryOptional.isEmpty()) {
            throw new ResourceNotFoundException("Category " + category + " not found");
        }
        Pageable pageable = getPageable(page);
        Page<PostEntity> postsPage = jpaPostRepository.findPostsByCategory(category, pageable);
        return getPosts(pageable, postsPage);
    }

    @Override
    public void deleteAll() {
        jpaPostRepository.deleteAllInBatch();
    }

    @Override
    public void deleteById(Long id) {
        jpaPostRepository.deleteById(id);
    }

    private static Pageable getPageable(Integer page) {
        int pageNo = page > 0 ? page - 1 : 0;
        return PageRequest.of(pageNo, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    private PagedResult<Post> getPosts(Pageable pageable, Page<PostEntity> postEntitiesPage) {
        if (postEntitiesPage.isEmpty()) {
            return new PagedResult<>(Page.empty());
        }
        var posts = postEntitiesPage.stream().map(postMapper::toModel).toList();
        Page<Post> postsPage = new PageImpl<>(posts, pageable, postEntitiesPage.getTotalElements());
        return new PagedResult<>(postsPage);
    }

    private CategoryEntity getOrCreateCategory(Category category) {
        categoryRepository.upsert(category);
        return jpaCategoryRepository.findByName(category.name()).orElseThrow();
    }
}
