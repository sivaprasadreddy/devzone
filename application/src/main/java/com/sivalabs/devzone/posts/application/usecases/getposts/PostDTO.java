package com.sivalabs.devzone.posts.application.usecases.getposts;

import java.time.LocalDateTime;

public record PostDTO(
        Long id,
        String url,
        String title,
        String category,
        Long createdUserId,
        String createdUserName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean editable) {}
