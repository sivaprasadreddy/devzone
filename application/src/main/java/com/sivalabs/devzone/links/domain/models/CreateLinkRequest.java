package com.sivalabs.devzone.links.domain.models;

import jakarta.validation.constraints.NotEmpty;

public class CreateLinkRequest {
    @NotEmpty(message = "URL should not be blank")
    private String url;

    private String title;
    private String category;
    private Long createdUserId;

    public CreateLinkRequest() {}

    public String getUrl() {
        return this.url;
    }

    public String getTitle() {
        return this.title;
    }

    public String getCategory() {
        return this.category;
    }

    public Long getCreatedUserId() {
        return this.createdUserId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCreatedUserId(Long createdUserId) {
        this.createdUserId = createdUserId;
    }
}
