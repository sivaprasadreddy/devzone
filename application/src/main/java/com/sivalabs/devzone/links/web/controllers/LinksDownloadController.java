package com.sivalabs.devzone.links.web.controllers;

import com.sivalabs.devzone.config.annotations.AdminOnly;
import com.sivalabs.devzone.links.domain.services.LinksExportService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AdminOnly
@RequiredArgsConstructor
@Slf4j
public class LinksDownloadController {
    private final LinksExportService linksExportService;

    @GetMapping("/links/download")
    public void downloadBookmarks(HttpServletResponse response) throws IOException {
        String filename = "links.csv";
        String mimeType = "text/csv";
        // TODO; fetching all the data at once is OutOfMemoryException waiting to happen
        byte[] csvData = linksExportService.getLinksCSVFileAsString();
        response.setContentType(mimeType);
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        response.getOutputStream().write(csvData);
    }
}
