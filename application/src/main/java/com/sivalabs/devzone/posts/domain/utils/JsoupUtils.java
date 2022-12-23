package com.sivalabs.devzone.posts.domain.utils;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsoupUtils {
    private static final Logger log = LoggerFactory.getLogger(JsoupUtils.class);

    public static String getTitle(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            return doc.title();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return url;
    }
}
