
import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshaller}
import cats.data._
import cats.instances.all._
import cats.syntax.all._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.{higherKinds, implicitConversions}


package object pfws {

  type Result[T] = EitherT[Future, String, T]
  type ReaderResult[T] = ReaderT[Result, Config, T]

  type ReaderMarshaller[T] = ToResponseMarshaller[Reader[Config, T]]

  implicit def resultMarshaller[T](implicit mf: ToResponseMarshaller[Future[Either[String, T]]], m: ToResponseMarshaller[Either[String, T]]): ToResponseMarshaller[Result[T]] =
    Marshaller.combined(_.value)

  implicit def readerMarshaller[T](implicit mf: ToResponseMarshaller[Result[T]], m: ToResponseMarshaller[Either[String, T]], c: Config): ToResponseMarshaller[ReaderResult[T]] =
    Marshaller.combined(_(c))

  implicit def resultFromValue[T](f: T): Result[T] = EitherT(Future.successful(Either.right(f)))
  implicit def resultFromXor[T](f: Either[String, T]): Result[T] = EitherT(Future.successful(f))
  implicit def resultFromFuture[T](f: Future[Either[String, T]]): Result[T] = EitherT(f)
  implicit def resultFromFutureValue[T](f: Future[T]): Result[T] = EitherT(f.map(v => Either.right[String, T](v)))

  implicit def fromValue[T](v: T)(implicit m: ToResponseMarshaller[T]): ReaderResult[T] = fromResult(resultFromValue(v))
  implicit def fromXor[T](f: Either[String, T]): ReaderResult[T] = fromResult(resultFromXor(f))
  implicit def fromResult[T](f: Result[T]): ReaderResult[T] = Kleisli.lift(f)

}
