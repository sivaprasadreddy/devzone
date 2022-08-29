package com.sivalabs.devzone.links.domain.models;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateLinkRequest {
    private Long id;

    @NotEmpty(message = "URL should not be blank")
    private String url;

    private String title;
    private String category;
}
