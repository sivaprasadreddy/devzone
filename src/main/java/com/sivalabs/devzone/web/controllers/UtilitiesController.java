package com.sivalabs.devzone.web.controllers;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UtilitiesController {

    @GetMapping("/page-metadata")
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
