package com.sivalabs.devzone.links.domain.models;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LinkDTO {
    private Long id;

    @NotBlank(message = "URL cannot be blank")
    private String url;

    private String title;
    private String category;
    private Long createdUserId;
    private String createdUserName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean editable;
}
