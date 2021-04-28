package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

    /** * Variables ** */
  def baseUrl: String = getProperty("loadtests.baseurl", "http://localhost:8080/home")
  def userCount: Int = getProperty("loadtests.users", "100").toInt
  def testDuration: Int = getProperty("loadtests.duration", "3000").toInt
  def rampDuration: Int = getProperty("loadtests.rampduration", "20").toInt

    /** * Helper Methods ***/
  protected def getProperty(propertyName: String, defaultValue: String) = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }

  val httpProtocol = http
    .baseUrl(baseUrl) // Here is the root for all relative URLs
    .acceptHeader("application/json") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

    /** * Before ***/
  before {
    println(s"baseUrl: ${baseUrl}")
    println(s"Running test with ${userCount} users")
    println(s"Ramping users over ${rampDuration} seconds")
    println(s"Total Test duration: ${testDuration} seconds")
  }

  val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
    .exec(http("request_1")
      .get("/"))
    .pause(7) // Note that Gatling has recorder real time pauses
    .exec(http("request_2")
      .get("/macbook"))
    .pause(2)
    .exec(http("request_3")
      .get("/computers"))
    .pause(3)
    .exec(http("request_4")
      .get("/"))
    .pause(2)
    .exec(http("request_5")
      .get("/automobile"))
    .pause(670 milliseconds)
    .exec(http("request_6")
      .get("/bottle"))
    .pause(629 milliseconds)
    .exec(http("request_7")
      .get("/telephone"))
    .pause(734 milliseconds)
    .exec(http("request_8")
      .get("/scissors"))
    .pause(5)
    .exec(http("request_9")
      .get("/mug"))
    .pause(1)
    .exec(http("request_10") // Here's an example of a POST request
      .post("/")
      .body(ElFileBody("Widget.json")).asJson
      .check(status.is(201)))

  /** * Setup Load Simulation ***/
  setUp(
    scn.inject(
      nothingFor(5 seconds),
      rampUsers(userCount).during(rampDuration seconds))
  ).protocols(httpProtocol).maxDuration(testDuration seconds)

  /** * After ***/
  after {
    println("Stress test complete")
  }
}
