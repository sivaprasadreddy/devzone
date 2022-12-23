package com.sivalabs.devzone.posts.usecases.getpagemetadata;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(controllers = PageMetadataController.class)
class PageMetadataControllerTest extends AbstractWebMvcTest {

    @Test
    void getPageMetadata() throws Exception {
        mockMvc.perform(get("/api/page-metadata?url=https://sivalabs.in"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath(
                                "$.title",
                                Matchers.equalTo("SivaLabs - My Experiments with Technology")));
    }
}
