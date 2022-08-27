package com.sivalabs.devzone.links.domain.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Category {
    private Long id;
    private String name;
    private Set<Link> links;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public Set<Link> getLinks() {
        if (this.links == null) {
            this.links = new HashSet<>();
        }
        return this.links;
    }
}
