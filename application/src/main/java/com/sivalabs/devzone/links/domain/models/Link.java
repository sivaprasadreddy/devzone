package com.sivalabs.devzone.links.domain.models;

import com.sivalabs.devzone.users.domain.models.User;
import java.time.LocalDateTime;

public record Link(
        Long id,
        String url,
        String title,
        Category category,
        User createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {}
