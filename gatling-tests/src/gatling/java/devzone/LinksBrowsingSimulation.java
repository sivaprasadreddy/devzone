package devzone;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

public class LinksBrowsingSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
        http.baseUrl("http://localhost:8080")
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate")
            .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0");

    FeederBuilder<String> searchFeeder = csv("data/feeders/search.csv").random();
    FeederBuilder<String> categoryFeeder = csv("data/feeders/categories.csv").random();

    ChainBuilder gotoPage = repeat(5, "n").on(
        exec(session -> session.set("pageNo", (int)session.get("n") + 1))
        .exec(http("Links Page #{pageNo}").get("/links?page=#{pageNo}"))
        .pause(1)
    );

    ChainBuilder byCategory = feed(categoryFeeder)
            .exec(http("Links By Category").get("/links?category=#{category}"))
            .pause(3);

    ChainBuilder search = feed(searchFeeder)
            .exec(http("Search").get("/links?query=#{query}"))
            .pause(3);

    ChainBuilder browseLinks = exec(gotoPage)
            .exec(byCategory)
            .exec(search);

/*
    ScenarioBuilder browseLinks = scenario("Browse Links")
            .during(Duration.ofMinutes(1), "Counter")
            .on(browseProducts);
*/

    ScenarioBuilder scnBrowseLinks = scenario("Browse Links").exec(browseLinks);

    {
        setUp(
            scnBrowseLinks.injectOpen(rampUsers(10).during(10))
        )
        .protocols(httpProtocol)
            .assertions(
                global().responseTime().max().lt(2000),
                global().successfulRequests().percent().gt(95.0)
            );
    }
}
