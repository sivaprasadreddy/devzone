package com.sivalabs.devzone.posts.domain.models;

import jakarta.validation.constraints.NotEmpty;

public record CreatePostRequest(
        @NotEmpty(message = "URL should not be blank") String url,
        String title,
        String category,
        Long createdUserId) {}
