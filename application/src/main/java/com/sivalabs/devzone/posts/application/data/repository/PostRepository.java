package com.sivalabs.devzone.posts.application.data.repository;

import com.sivalabs.devzone.common.model.PagedResult;
import com.sivalabs.devzone.posts.domain.model.Post;
import java.util.Optional;

public interface PostRepository {
    PagedResult<Post> getAllPosts(Integer page);

    PagedResult<Post> searchPosts(String query, Integer page);

    PagedResult<Post> getPostsByCategory(String category, Integer page);

    Optional<Post> findById(Long id);

    Post save(Post post);

    void deleteAll();

    void deleteById(Long id);
}
