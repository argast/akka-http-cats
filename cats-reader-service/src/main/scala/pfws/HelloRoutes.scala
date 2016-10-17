package pfws

import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshaller}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.data._

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions


object HelloRoutes {

  implicit def xorMarshaller[T](implicit m: ToResponseMarshaller[T]): ToResponseMarshaller[Xor[String, T]] = Marshaller.combined(_.toEither)

  implicit def resultMarshaller[T](implicit mf: ToResponseMarshaller[Future[Xor[String, T]]], m: ToResponseMarshaller[Xor[String, T]]): ToResponseMarshaller[Result[T]] =
    Marshaller.combined(_.value)

  implicit def readerMarshaller[T](implicit mf: ToResponseMarshaller[Result[T]], m: ToResponseMarshaller[Xor[String, T]], c: Config): ToResponseMarshaller[ReaderResult[T]] =
    Marshaller.combined(_(c))

  def hello: ReaderResult[String] = "Hello".liftToReader

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
