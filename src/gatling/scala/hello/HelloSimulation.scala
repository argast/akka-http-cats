package hello


import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.language.postfixOps

class HelloSimulation extends Simulation {

  val httpConf = http.baseURL(s"http://${GatlingTestConfig.hostname}:8090")

  val scn = scenario("Hello Simulation").during(10 seconds) {
    exec(
      http("hello").get("/hello")
    )
  }

  setUp(scn.inject(atOnceUsers(10))).protocols(httpConf)
}
