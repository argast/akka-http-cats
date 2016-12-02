package pfws

import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshaller}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.data._
import cats.instances.all._
import cats.syntax.all._

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions


object HelloRoutes {

  def hello: ReaderResult[String] = "Hello"

  def routes(implicit c: Config, ec: ExecutionContext): Route = {
    path("greeting") {
      complete {
        hello
      }
    } ~ path("hello") {
      complete {
        "Hello world!"
        //        for {
//          greeting <- GreetingService.getGreeting
//          separator <- Database.get("separator")
//        } yield greeting + separator + "world!"
      }
    }
  }
}
