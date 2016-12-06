package hello

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import hello.routes.HelloRoutes
import org.scalatest.{FunSpec, Matchers}


class RoutesTest extends FunSpec with ScalatestRouteTest with Matchers {

  implicit val config = new UnitTestConfig {
  }

  describe("Hello hello.routes") {
    it("should return correct response") {
      Get("/hello") ~> HelloRoutes.routes ~> check {
        status should equal (StatusCodes.OK)
        responseAs[String] shouldEqual "Hello world!"
      }

    }
  }
}
