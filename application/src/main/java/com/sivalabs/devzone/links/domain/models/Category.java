package com.sivalabs.devzone.links.domain.models;

import java.time.LocalDateTime;
import java.util.Set;

public record Category(
        Long id, String name, Set<Link> links, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public Category(Long id, String name) {
        this(id, name, Set.of(), null, null);
    }
}
