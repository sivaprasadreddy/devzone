package devzone;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import utils.SimulationHelper;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static utils.SimulationHelper.getConfig;

public class LinkCreationSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = SimulationHelper.getHttpProtocolBuilder();

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

    ScenarioBuilder scnCreateLink = scenario("Create Link")
            .during(Duration.ofMinutes(2), "Counter")
            .on(createLinkFlow);

    //ScenarioBuilder scnCreateLink = scenario("Create Link").exec(createLinkFlow);

    {
        setUp(
            scnCreateLink.injectOpen(rampUsers(getConfig().getInt("users")).during(10))
        )
        .protocols(httpProtocol)
            .assertions(
                global().responseTime().max().lt(800),
                global().successfulRequests().percent().is(100.0)
            );
    }
}
