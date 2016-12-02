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

class InfoTest extends WordSpec with ScalaFutures with Matchers {

  implicit val as = ActorSystem()
  implicit val mat = ActorMaterializer()
  implicit val ec = as.dispatcher

  override implicit val patienceConfig = PatienceConfig(scaled(Span(5, Seconds)), scaled(Span(1, Second)))

  "Info page" should {
    "return successfully" in {
      val response = Http().singleRequest(Get(s"http://${IntegrationTestConfig.hostname}:8090"))
      whenReady(response) { r =>
        r.status shouldEqual StatusCodes.OK
      }
    }
  }
}
