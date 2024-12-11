package com.sivalabs.devzone.posts.web.controllers;

import com.sivalabs.devzone.config.annotations.AdminOnly;
import com.sivalabs.devzone.posts.application.usecases.downloadposts.DownloadPostsHandler;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AdminOnly
public class DownloadPostsController {
    private final DownloadPostsHandler downloadPostsHandler;

    public DownloadPostsController(DownloadPostsHandler downloadPostsHandler) {
        this.downloadPostsHandler = downloadPostsHandler;
    }

    @GetMapping("/posts/download")
    public void downloadBookmarks(HttpServletResponse response) throws IOException {
        String filename = "posts.csv";
        String mimeType = "text/csv";
        // fetching all the data at once is OutOfMemoryException waiting to happen
        byte[] csvData = downloadPostsHandler.getPostsCSVFileAsString();
        response.setContentType(mimeType);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        response.getOutputStream().write(csvData);
    }
}
