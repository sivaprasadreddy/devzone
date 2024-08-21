package com.sivalabs.devzone.posts.application.usecases.createpost;

import com.sivalabs.devzone.posts.domain.utils.JsoupUtils;
import jakarta.validation.constraints.NotEmpty;
import org.apache.commons.lang3.StringUtils;

public record CreatePostRequest(
        @NotEmpty(message = "URL should not be blank") String url,
        String title,
        String category,
        Long createdUserId) {
    public String derivedTitle() {
        return StringUtils.isNotEmpty(title) ? title : JsoupUtils.getTitle(url);
    }
}
