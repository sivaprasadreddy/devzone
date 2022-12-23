package com.sivalabs.devzone.links.domain.models;

import jakarta.validation.constraints.NotEmpty;

public record UpdateLinkRequest(
        Long id,
        @NotEmpty(message = "URL should not be blank") String url,
        String title,
        String category) {}
