package com.sivalabs.devzone.links.domain.models;

import jakarta.validation.constraints.NotEmpty;

public class UpdateLinkRequest {
    private Long id;

    @NotEmpty(message = "URL should not be blank")
    private String url;

    private String title;
    private String category;

    public Long getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public String getTitle() {
        return this.title;
    }

    public String getCategory() {
        return this.category;
    }

    public void setId(Long id) {
        this.id = id;
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
}
