package devzone;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static utils.SimulationHelper.getConfig;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import utils.SimulationHelper;

import java.time.Duration;

public class LinksBrowsingSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = SimulationHelper.getHttpProtocolBuilder();

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

    ScenarioBuilder scnBrowseLinks = scenario("Browse Links")
            .during(Duration.ofMinutes(2), "Counter")
            .on(browseLinks);

    //ScenarioBuilder scnBrowseLinks = scenario("Browse Links").exec(browseLinks);

    {
        setUp(
            scnBrowseLinks.injectOpen(rampUsers(getConfig().getInt("users")).during(10))
        )
        .protocols(httpProtocol)
            .assertions(
                global().responseTime().max().lt(800),
                global().successfulRequests().percent().is(100.0)
            );
    }
}
