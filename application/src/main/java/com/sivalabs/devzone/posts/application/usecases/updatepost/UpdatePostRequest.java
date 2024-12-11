package com.sivalabs.devzone.posts.application.usecases.updatepost;

import com.sivalabs.devzone.posts.domain.utils.JsoupUtils;
import jakarta.validation.constraints.NotEmpty;
import org.apache.commons.lang3.StringUtils;

public record UpdatePostRequest(
        Long id, @NotEmpty(message = "URL should not be blank") String url, String title, String category) {
    public String derivedTitle() {
        return StringUtils.isNotEmpty(title) ? title : JsoupUtils.getTitle(url);
    }
}
