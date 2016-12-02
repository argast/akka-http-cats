import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FunSpec, Matchers}
import pfws._


class RoutesTest extends FunSpec with ScalatestRouteTest with Matchers {

  implicit val config = new UnitTestConfig {
    override val greetingUrl: String = ""
  }

  describe("Hello routes") {
    it("should return correct response") {
      Get("/hello") ~> HelloRoutes.routes ~> check {
        status should equal (StatusCodes.OK)
        responseAs[String] shouldEqual "Hello world!"
      }

    }
  }
}
