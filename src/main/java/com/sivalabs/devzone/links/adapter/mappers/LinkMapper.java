package com.sivalabs.devzone.links.adapter.mappers;

import com.sivalabs.devzone.links.adapter.entities.LinkEntity;
import com.sivalabs.devzone.links.domain.models.Link;
import com.sivalabs.devzone.users.adapter.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkMapper {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public LinkEntity toEntity(Link link) {
        LinkEntity linkEntity = new LinkEntity();
        linkEntity.setId(link.getId());
        linkEntity.setUrl(link.getUrl());
        linkEntity.setTitle(link.getTitle());
        linkEntity.setCreatedBy(userMapper.toEntity(link.getCreatedBy()));
        linkEntity.setCreatedAt(link.getCreatedAt());
        linkEntity.setUpdatedAt(link.getUpdatedAt());
        linkEntity.setCategory(categoryMapper.toEntity(link.getCategory()));
        return linkEntity;
    }

    public Link toModel(LinkEntity link) {
        Link dto = new Link();
        dto.setId(link.getId());
        dto.setUrl(link.getUrl());
        dto.setTitle(link.getTitle());
        dto.setCreatedBy(userMapper.toModel(link.getCreatedBy()));
        dto.setCreatedAt(link.getCreatedAt());
        dto.setUpdatedAt(link.getUpdatedAt());
        dto.setCategory(categoryMapper.toModel(link.getCategory()));
        return dto;
    }
}
