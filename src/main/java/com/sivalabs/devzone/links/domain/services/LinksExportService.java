package com.sivalabs.devzone.links.domain.services;

import com.sivalabs.devzone.links.domain.models.Link;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LinksExportService {
    private final LinkService linkService;

    public byte[] getLinksCSVFileAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("url,title,category").append(System.lineSeparator());
        List<Link> allLinks = linkService.getAllLinks();
        for (Link linkDTO : allLinks) {
            String category = linkDTO.getCategory() == null? null:linkDTO.getCategory().getName();
            sb.append(
                            String.join(
                                    ",",
                                    linkDTO.getUrl(),
                                    "\"" + linkDTO.getTitle() + "\"",
                                category))
                    .append(System.lineSeparator());
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
