package com.sivalabs.devzone.links.domain.models;

import java.time.LocalDateTime;

public record LinkDTO(
        Long id,
        String url,
        String title,
        String category,
        Long createdUserId,
        String createdUserName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean editable) {}
