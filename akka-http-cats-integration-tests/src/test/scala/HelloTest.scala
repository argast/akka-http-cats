import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.model.StatusCodes
import akka.stream.ActorMaterializer
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Second, Seconds, Span}
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration._
import scala.language.postfixOps

class HelloTest extends WordSpec with ScalaFutures with Matchers {

  implicit val as = ActorSystem()
  implicit val mat = ActorMaterializer()
  implicit val ec = as.dispatcher

  override implicit val patienceConfig = PatienceConfig(scaled(Span(5, Seconds)), scaled(Span(1, Second)))

  "Hello service" should {
    "return hello" in {
      val response = Http().singleRequest(Get(s"http://${TestConfig.hostname}:8090/hello"))
      whenReady(response) { r =>
        r.status shouldEqual StatusCodes.OK
        whenReady(r.entity.toStrict(1 second)) { e =>
          e.data.utf8String shouldEqual "Hello world!"
        }
      }
    }
  }
}
