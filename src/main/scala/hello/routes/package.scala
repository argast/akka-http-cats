package hello

import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshaller}
import cats.data._
import cats.syntax.all._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.{higherKinds, implicitConversions}
import result._

package object routes {

  implicit def resultMarshaller[T](implicit mf: ToResponseMarshaller[Future[Either[String, T]]], m: ToResponseMarshaller[Either[String, T]]): ToResponseMarshaller[Result[T]] =
    Marshaller.combined(_.value)

  implicit def readerMarshaller[T](implicit mf: ToResponseMarshaller[Result[T]], m: ToResponseMarshaller[Either[String, T]], c: Config): ToResponseMarshaller[ReaderResult[T]] =
    Marshaller.combined(_(c))

}
