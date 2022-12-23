package com.sivalabs.devzone.links.domain.models;

import java.time.LocalDateTime;
import java.util.Set;

public class Category {
    private Long id;
    private String name;
    private Set<Link> links;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category other)) {
            return false;
        }
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Set<Link> getLinks() {
        return this.links;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
