package devzone

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class DevZoneSimulation extends Simulation {

  val httpConf = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  // Now, we can write the scenario as a composition
  val scnBrowseBookmarks = scenario("Browse Bookmarks")
    .during(5.minutes, "Counter") {
      exec(Browse.bookmarks).pause(2)
    }

  setUp(
      scnBrowseBookmarks.inject(rampUsers(500) during (10.seconds))
      //,scnBrowseBookmarks.inject(atOnceUsers(100))
  ).protocols(httpConf)
    .assertions(
      global.responseTime.max.lt(800),
      global.successfulRequests.percent.gt(95)
    )

}

object Browse {
  val searchFeeder = csv("data/search.csv").random
  val tagFeeder = csv("data/tags.csv").random

  val byTag = feed(tagFeeder)
    .exec(http("Bookmarks By Tag")
      .get("/links?tag=${tag}"))
    .pause(3)

  val search = feed(searchFeeder)
    .exec(http("Search")
      .get("/links?query=${key}"))
    .pause(3)

  val gotoPage = repeat(5, "n") {
    exec{ session => session.set("pageNo", session("n").as[Int] + 1) }
    .exec(http("Bookmarks Page ${pageNo}").get("/links?page=${pageNo}"))
    .pause(1)
  }

  val bookmarks =
    exec(gotoPage)
      .exec(search)
      .exec(byTag)

}
