package devzone;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class LinkCreationSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
        http.baseUrl("http://localhost:8080")
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate")
            .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0");

    FeederBuilder<String> linkFeeder = csv("data/feeders/links.csv").random();
    FeederBuilder<String> credentialsFeeder = csv("data/feeders/credentials.csv").random();

    ChainBuilder login = feed(credentialsFeeder)
        .exec(http("Login Form")
            .get("/login")
            .check(css("input[name=_csrf]", "value").saveAs("csrf"))
        ).pause(1)
            .exec(http("Login")
                    .post("/login")
                    .formParam("_csrf", "#{csrf}")
                    .formParam("username", "#{username}")
                    .formParam("password", "#{password}")
            ).pause(1);

    ChainBuilder createLink = feed(linkFeeder)
            .exec(http("New Link Form")
                .get("/links/new")
                .check(css("input[name=_csrf]", "value").saveAs("csrf"))
            ).pause(1)
            .exec(
                http("Create New Link")
                .post("/links")
                    .formParam("_csrf", "#{csrf}")
                    .formParam("url", "#{url}")
                    .formParam("title", "#{title}")
                    .formParam("category", "#{category}")
            ).pause(1);

    ChainBuilder createLinkFlow =
            exec(login)
            .pause(2)
            .exec(createLink);

    /*ScenarioBuilder scnCreateLink = scenario("Create Link")
            .during(Duration.ofMinutes(1), "Counter")
            .on(createOrderFlow);*/

    ScenarioBuilder scnCreateLink = scenario("Create Link").exec(createLinkFlow);

    {
        setUp(
            scnCreateLink.injectOpen(rampUsers(10).during(10))
        )
        .protocols(httpProtocol)
            .assertions(
                global().responseTime().max().lt(2000),
                global().successfulRequests().percent().gt(95.0)
            );
    }
}
