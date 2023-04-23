package com.sivalabs.devzone.posts.adapter.data.mapper;

import com.sivalabs.devzone.posts.adapter.data.entity.PostEntity;
import com.sivalabs.devzone.posts.domain.model.Post;
import com.sivalabs.devzone.users.adapter.data.mapper.UserMapper;

import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public PostMapper(CategoryMapper categoryMapper, UserMapper userMapper) {
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
    }

    public PostEntity toEntity(Post post) {
        PostEntity postEntity = new PostEntity();
        postEntity.setId(post.id());
        postEntity.setUrl(post.url());
        postEntity.setTitle(post.title());
        postEntity.setCreatedBy(userMapper.toEntity(post.createdBy()));
        postEntity.setCreatedAt(post.createdAt());
        postEntity.setUpdatedAt(post.updatedAt());
        postEntity.setCategory(categoryMapper.toEntity(post.category()));
        return postEntity;
    }

    public Post toModel(PostEntity post) {
        return new Post(
                post.getId(),
                post.getUrl(),
                post.getTitle(),
                categoryMapper.toModel(post.getCategory()),
                userMapper.toModel(post.getCreatedBy()),
                post.getCreatedAt(),
                post.getUpdatedAt());
    }
}
