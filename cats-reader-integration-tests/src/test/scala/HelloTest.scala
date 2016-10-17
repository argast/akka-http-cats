import com.jayway.restassured.RestAssured
import org.hamcrest.Matchers.equalTo
import org.scalatest.FunSpec

class HelloTest extends FunSpec {

  describe("Hello service") {
    it("should return hello") {
      RestAssured.get("http://192.168.99.100:8090/hello").then().body(equalTo("Hello world!"))
    }
  }
}
