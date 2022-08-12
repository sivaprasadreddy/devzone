package com.sivalabs.devzone.domain.services;

import com.sivalabs.devzone.domain.models.LinkDTO;
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
        List<LinkDTO> allLinks = linkService.getAllLinks();
        for (LinkDTO linkDTO : allLinks) {
            sb.append(
                            String.join(
                                    ",",
                                    linkDTO.getUrl(),
                                    "\"" + linkDTO.getTitle() + "\"",
                                    linkDTO.getCategory()))
                    .append(System.lineSeparator());
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
