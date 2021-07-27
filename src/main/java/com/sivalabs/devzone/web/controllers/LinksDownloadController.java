package com.sivalabs.devzone.web.controllers;

import com.sivalabs.devzone.config.annotations.AdminOnly;
import com.sivalabs.devzone.domain.services.LinksExportService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LinksDownloadController {
    private final LinksExportService linksExportService;

    @GetMapping("/links/download")
    @AdminOnly
    public void downloadBookmarks(HttpServletResponse response) throws IOException {
        String filename = "links.csv";
        String mimeType = "text/csv";
        byte[] csvData = linksExportService.getLinksCSVFileAsString();
        response.setContentType(mimeType);
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        response.getOutputStream().write(csvData);
    }
}
