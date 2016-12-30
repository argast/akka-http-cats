package hello

import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshaller}
import cats.data._
import cats.syntax.all._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.{higherKinds, implicitConversions}


package object result {

  type FutureEither[T] = EitherT[Future, String, T]
  type Result[T] = ReaderT[FutureEither, Config, T]

  object implicits {
    implicit def resultFromValue[T](f: T): FutureEither[T] = EitherT(Future.successful(Either.right(f)))
    implicit def resultFromXor[T](f: Either[String, T]): FutureEither[T] = EitherT(Future.successful(f))
    implicit def resultFromFuture[T](f: Future[Either[String, T]]): FutureEither[T] = EitherT(f)
    implicit def resultFromFutureValue[T](f: Future[T]): FutureEither[T] = EitherT(f.map(v => Either.right[String, T](v)))

    //implicit def fromValue[T](v: T): Result[T] = fromResult(resultFromValue(v))
    implicit def fromXor[T](f: Either[String, T]): Result[T] = fromResult(resultFromXor(f))
    implicit def fromResult[T](f: FutureEither[T]): Result[T] = Kleisli.lift(f)
  }




  implicit class ReaderResultOps[T](v: T) {
    import implicits._
    def liftR: Result[T] = fromResult(resultFromValue(v))
  }

}
