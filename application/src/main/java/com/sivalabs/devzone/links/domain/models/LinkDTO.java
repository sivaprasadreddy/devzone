package com.sivalabs.devzone.links.domain.models;

import java.time.LocalDateTime;

public class LinkDTO {
    private Long id;
    private String url;
    private String title;
    private String category;
    private Long createdUserId;
    private String createdUserName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean editable;

    public LinkDTO() {}

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

    public Long getCreatedUserId() {
        return this.createdUserId;
    }

    public String getCreatedUserName() {
        return this.createdUserName;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public boolean isEditable() {
        return this.editable;
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

    public void setCreatedUserId(Long createdUserId) {
        this.createdUserId = createdUserId;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
