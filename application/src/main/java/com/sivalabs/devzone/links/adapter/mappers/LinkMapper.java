package com.sivalabs.devzone.links.adapter.mappers;

import com.sivalabs.devzone.links.adapter.entities.LinkEntity;
import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.users.adapter.mappers.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class LinkMapper {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public LinkMapper(CategoryMapper categoryMapper, UserMapper userMapper) {
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
    }

    public LinkEntity toEntity(Link link) {
        LinkEntity linkEntity = new LinkEntity();
        linkEntity.setId(link.id());
        linkEntity.setUrl(link.url());
        linkEntity.setTitle(link.title());
        linkEntity.setCreatedBy(userMapper.toEntity(link.createdBy()));
        linkEntity.setCreatedAt(link.createdAt());
        linkEntity.setUpdatedAt(link.updatedAt());
        linkEntity.setCategory(categoryMapper.toEntity(link.category()));
        return linkEntity;
    }

    public Link toModel(LinkEntity link) {
        return new Link(
                link.getId(),
                link.getUrl(),
                link.getTitle(),
                categoryMapper.toModel(link.getCategory()),
                userMapper.toModel(link.getCreatedBy()),
                link.getCreatedAt(),
                link.getUpdatedAt());
    }
}
