
import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshaller}
import cats.data._
import cats.instances.all._
import cats.syntax.all._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.{higherKinds, implicitConversions}


package object pfws {

  type Result[T] = XorT[Future, String, T]
  type ReaderResult[T] = ReaderT[Result, Config, T]

  type ReaderMarshaller[T] = ToResponseMarshaller[Reader[Config, T]]

  implicit def xorMarshaller[T](implicit m: ToResponseMarshaller[T]): ToResponseMarshaller[Xor[String, T]] = Marshaller.combined(_.toEither)

  implicit def resultMarshaller[T](implicit mf: ToResponseMarshaller[Future[Xor[String, T]]], m: ToResponseMarshaller[Xor[String, T]]): ToResponseMarshaller[Result[T]] =
    Marshaller.combined(_.value)

  implicit def readerMarshaller[T](implicit mf: ToResponseMarshaller[Result[T]], m: ToResponseMarshaller[Xor[String, T]], c: Config): ToResponseMarshaller[ReaderResult[T]] =
    Marshaller.combined(_(c))

  implicit class ValueToReader[T](v: T) {
    def liftToReader: ReaderResult[T] = resultFromValue(v)
  }

  implicit def resultFromValue[T](f: T): Result[T] = Future.successful(f).rightXorT[String]
  implicit def resultFromXor[T](f: Xor[String, T]): Result[T] = XorT(Future.successful(f))
  implicit def resultFromFuture[T](f: Future[Xor[String, T]]): Result[T] = XorT(f)
  implicit def resultFromFutureValue[T](f: Future[T]): Result[T] = f.rightXorT[String]

  implicit def fromValue[T](v: T)(implicit m: ToResponseMarshaller[T]): ReaderResult[T] = fromResult(resultFromValue(v))
  implicit def fromXor[T](f: Xor[String, T]): ReaderResult[T] = fromResult(resultFromXor(f))
  implicit def fromResult[T](f: Result[T]): ReaderResult[T] = Kleisli.lift(f)

}
