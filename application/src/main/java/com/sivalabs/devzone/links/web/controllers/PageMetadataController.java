package com.sivalabs.devzone.links.web.controllers;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageMetadataController {

    private static final Logger log = LoggerFactory.getLogger(PageMetadataController.class);

    @GetMapping("/api/page-metadata")
    public Map<String, String> getPageMetadata(@RequestParam String url) {
        Map<String, String> metadata = new ConcurrentHashMap<>();
        try {
            Document doc = Jsoup.connect(url).get();
            metadata.put("title", doc.title());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return metadata;
    }
}
