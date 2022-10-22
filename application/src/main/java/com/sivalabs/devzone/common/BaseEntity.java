package com.sivalabs.devzone.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Setter
@Getter
public abstract class BaseEntity {

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(insertable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    protected abstract Long getId();

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
