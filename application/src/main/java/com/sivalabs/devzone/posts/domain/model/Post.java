package com.sivalabs.devzone.posts.domain.model;

import com.sivalabs.devzone.users.domain.model.User;

import java.time.LocalDateTime;

public record Post(
        Long id,
        String url,
        String title,
        Category category,
        User createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {}
