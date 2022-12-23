package com.sivalabs.devzone.posts.domain.models;

import static com.sivalabs.devzone.posts.domain.utils.StringUtils.toSlug;

import java.time.LocalDateTime;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public record Category(
        Long id, String name, Set<Post> posts, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public Category(Long id, String name) {
        this(id, name, Set.of(), null, null);
    }

    public static Category buildCategory(String categoryName) {
        if (StringUtils.isBlank(categoryName)) {
            return null;
        }
        String name = toSlug(categoryName.trim());
        return new Category(null, name);
    }
}
