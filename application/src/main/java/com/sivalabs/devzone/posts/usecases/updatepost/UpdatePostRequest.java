package com.sivalabs.devzone.posts.usecases.updatepost;

import jakarta.validation.constraints.NotEmpty;

public record UpdatePostRequest(
        Long id,
        @NotEmpty(message = "URL should not be blank") String url,
        String title,
        String category) {}
