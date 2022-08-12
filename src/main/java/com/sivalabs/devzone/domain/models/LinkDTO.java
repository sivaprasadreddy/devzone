package com.sivalabs.devzone.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LinkDTO {
    private Long id;

    @NotBlank(message = "URL cannot be blank")
    private String url;

    private String title;

    @JsonProperty("created_user_id")
    private Long createdUserId;

    @JsonProperty("created_user_name")
    private String createdUserName;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    private String category;

    private boolean editable;
}
