package com.sivalabs.devzone.links.domain.services;

import com.sivalabs.devzone.common.PagedResult;
import com.sivalabs.devzone.links.domain.models.Link;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LinksExportService {
    private final LinkService linkService;

    public LinksExportService(LinkService linkService) {
        this.linkService = linkService;
    }

    public byte[] getLinksCSVFileAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("url,title,category").append(System.lineSeparator());
        PagedResult<Link> pagedResult = linkService.getLinks(1);
        this.addLinks(sb, pagedResult.getData());
        int totalPages = pagedResult.getTotalPages();
        for (int page = 2; page <= totalPages; page++) {
            pagedResult = linkService.getLinks(page);
            this.addLinks(sb, pagedResult.getData());
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void addLinks(StringBuilder sb, List<Link> allLinks) {
        for (Link linkDTO : allLinks) {
            String category = linkDTO.category() == null ? "" : linkDTO.category().name();
            sb.append(String.join(",", linkDTO.url(), "\"" + linkDTO.title() + "\"", category))
                    .append(System.lineSeparator());
        }
    }
}
