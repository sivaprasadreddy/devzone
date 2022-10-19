package com.sivalabs.devzone.links.domain.models;

import java.time.LocalDateTime;
import lombok.Data;

@Data
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
}
