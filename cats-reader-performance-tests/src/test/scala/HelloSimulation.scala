
import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._

class HelloSimulation extends Simulation {

  val httpConf = http.baseURL("http://192.168.99.100:8090")

  val scn = scenario("Hello Simulation").exec(
    http("hello").get("/hello")
  )

  setUp(scn.inject(atOnceUsers(10))).protocols(httpConf)
}
