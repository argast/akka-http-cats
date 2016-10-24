package pfws

import cats.instances.all._
import cats.syntax.all._
import cats.data._
import akka.http.scaladsl.client.RequestBuilding._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import pfws._

object GreetingService {

  def getGreeting: ReaderResult[String] = ReaderT { config =>
//    val p = config.pipeline ~> unmarshal[String]
//    p(Get(config.greetingUrl + "/greeting"))
    "".right[String]
  }

}
