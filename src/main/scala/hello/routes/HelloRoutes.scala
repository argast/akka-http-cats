package hello.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.StrictLogging
import hello.Config

import scala.concurrent.Future
import scala.language.implicitConversions
import hello.result._
import cats.instances.future._
import hello.result.implicits._


object HelloRoutes extends StrictLogging {

  def hello: Result[String] = "Hello".liftR

  def routes(implicit config: Config): Route = {
    path("greeting") {
      complete {
        hello
      }
    } ~ path("hello") {
      complete {
        import config._
        logger.info("Before Hello")
        Future {
          logger.info("Hello")
          "Hello world!"
        }
      }
    } ~ path("hello" / Segment) { language =>
      complete {
        import config._
        for {
          translation <- translations.translate(language)
        } yield translation.map(_ + "!!!")
      }
    }
  }
}
