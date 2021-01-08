package com.sivalabs.devzone.web.api.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.domain.models.LinksDTO;
import com.sivalabs.devzone.domain.services.LinkService;
import com.sivalabs.devzone.domain.services.SecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;

@WebMvcTest(controllers = LinkRestController.class)
class LinkRestControllerTest extends AbstractWebMvcTest {
    @MockBean protected LinkService linkService;

    @MockBean protected SecurityService securityService;

    @Test
    void shouldFetchLinksFirstPage() throws Exception {
        LinksDTO linksDTO = new LinksDTO();
        given(linkService.getAllLinks(any(Pageable.class))).willReturn(linksDTO);

        this.mockMvc.perform(get("/api/links")).andExpect(status().isOk());
    }
}
