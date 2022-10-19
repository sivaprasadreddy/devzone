package com.sivalabs.devzone.utils;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

public class LinkReaderUtils {
    public static void main(String[] args) throws Exception {
        fromDZone();
    }

    static void fromDZone() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Integer> portalCodeMap =
                Map.of(
                        "Java", 1,
                        "DevOps", 6,
                        "MicroServices", 6001,
                        "WebDev", 11,
                        "Cloud", 4);
        for (Map.Entry<String, Integer> entry : portalCodeMap.entrySet()) {
            String portalName = entry.getKey();
            Integer portalCode = entry.getValue();
            File file = new File("dzone-links-" + portalName.toLowerCase() + ".csv");
            FileWriter fr = new FileWriter(file);
            PrintWriter pr = new PrintWriter(fr);
            pr.println("url,title,category");

            for (int page = 1; page <= 50; page++) {
                DZoneResponse response =
                        restTemplate.getForObject(
                                "https://dzone.com/services/widget/article-listV2/list?page="
                                        + page
                                        + "&portal="
                                        + portalCode
                                        + "&sort=newest",
                                DZoneResponse.class);
                List<DZoneArticle> articles = response.getResult().getData().getNodes();
                System.out.println("portalName=" + portalName + ", Page = " + page);
                for (DZoneArticle article : articles) {
                    pr.println(
                            "https://dzone.com"
                                    + article.getArticleLink()
                                    + ",\""
                                    + article.getTitle()
                                    + "\","
                                    + portalName);
                }
                Thread.sleep(1000);
            }
            pr.close();
        }
    }

    static void fromRss() throws IOException {
        RssReader reader = new RssReader();
        List<String> feedUrls =
                List.of(
                        "https://www.sivalabs.in/index.xml",
                        "https://reflectoring.io/index.xml",
                        "https://rieckpil.de/feed/");
        File file = new File("links.csv");
        FileWriter fr = new FileWriter(file);
        PrintWriter pr = new PrintWriter(fr);
        pr.println("url,title,category");
        for (String url : feedUrls) {
            Stream<Item> rssFeed = reader.read(url);
            List<Item> articles = rssFeed.toList();
            System.out.println("Total articles: " + articles.size());
            for (Item article : articles) {
                if (article.getLink().isPresent()) {
                    System.out.println("Title = " + article.getTitle().orElse(null));
                    System.out.println("link = " + article.getLink().orElse(null));
                    System.out.println("guid = " + article.getGuid().orElse(null));
                    System.out.println("description = " + article.getDescription().orElse(null));
                    System.out.println("pubDate = " + article.getPubDate().orElse(null));
                    System.out.println("========================================================");
                    pr.println(
                            article.getLink().get()
                                    + ",\""
                                    + article.getTitle().orElse(null)
                                    + "\""
                                    + ",Java");
                }
            }
        }
        pr.close();
    }
}

class DZoneResponse {
    private DZoneResult result;

    public DZoneResult getResult() {
        return result;
    }

    public void setResult(DZoneResult result) {
        this.result = result;
    }
}

class DZoneResult {
    private DZoneData data;

    public DZoneData getData() {
        return data;
    }

    public void setData(DZoneData data) {
        this.data = data;
    }
}

@Data
class DZoneData {
    private List<DZoneArticle> nodes;

    public List<DZoneArticle> getNodes() {
        return nodes;
    }

    public void setNodes(List<DZoneArticle> nodes) {
        this.nodes = nodes;
    }
}

class DZoneArticle {
    private String title;
    private String articleLink;
    private String category;

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
