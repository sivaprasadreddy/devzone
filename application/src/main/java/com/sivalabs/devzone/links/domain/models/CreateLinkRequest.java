package com.sivalabs.devzone.links.domain.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateLinkRequest {
    @NotEmpty(message = "URL should not be blank")
    private String url;

    private String title;
    private String category;
    private Long createdUserId;
}
