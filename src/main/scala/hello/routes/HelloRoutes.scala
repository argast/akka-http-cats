package hello.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.StrictLogging
import hello.Config

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions


object HelloRoutes extends StrictLogging {

  def hello: ReaderResult[String] = "Hello"

  def routes(implicit c: Config, ec: ExecutionContext): Route = {
    path("greeting") {
      complete {
        hello
      }
    } ~ path("hello") {
      complete {
        Future {
          logger.info("Hello")
          "Hello world!"
        }
      }
    }
  }
}
