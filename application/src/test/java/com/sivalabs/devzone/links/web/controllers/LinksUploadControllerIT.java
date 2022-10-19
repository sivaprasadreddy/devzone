package com.sivalabs.devzone.links.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;

public class LinksUploadControllerIT extends AbstractIntegrationTest {

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    void showUploadBookmarksPage() throws Exception {
        mockMvc.perform(get("/links/upload"))
                .andExpect(status().isOk())
                .andExpect(view().name("upload-links"));
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    void uploadBookmarks() throws Exception {
        byte[] bytes = new ClassPathResource("data/links-test.csv").getInputStream().readAllBytes();
        MockMultipartFile file = new MockMultipartFile("file", "links-test.csv", "text/csv", bytes);
        mockMvc.perform(multipart("/links/upload").file(file).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("msg", "Links imported successfully"))
                .andExpect(header().string("Location", "/links/upload"));
    }
}
