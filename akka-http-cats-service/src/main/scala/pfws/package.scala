
import akka.http.scaladsl.marshalling.ToResponseMarshaller

import scala.concurrent.{ExecutionContext, Future}
import language.implicitConversions
import language.higherKinds
import cats.instances.all._
import cats.syntax.all._
import cats.data._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


package object pfws {

  type Result[T] = XorT[Future, String, T]
  type ReaderResult[T] = ReaderT[Result, Config, T]

  type ReaderMarshaller[T] = ToResponseMarshaller[Reader[Config, T]]

//  implicit def xorMarshaller[T](implicit m: Marshaller[Either[String, T]], ec: ExecutionContext): Marshaller[Xor[String, T]] =
//    Marshaller[Xor[String, T]] { (value, ctx) ⇒
//      m(value.toEither, ctx)
//    }

//  implicit def resultMarshaller[T](implicit m: Marshaller[Future[T]], mx: Marshaller[T], ec: ExecutionContext): Marshaller[Result[T]] =
//    Marshaller[Result[T]] { (value, ctx) ⇒
//      m(value.value, ctx)
//    }

  implicit class ValueToReader[T](v: T) {
    def liftToReader: ReaderResult[T] = resultFromValue(v)
  }

  //implicit def fromValue[T](v: T): ReaderResult[T] = ReaderT[Result, Config, T](_ => XorT(Future.successful(v.right[String])))
  implicit def resultFromValue[T](f: T): Result[T] = XorT(Future.successful(f.right[String]))
  implicit def resultFromXor[T](f: Xor[String, T]): Result[T] = XorT(Future.successful(f))
  implicit def resultFromFuture[T](f: Future[Xor[String, T]]): Result[T] = XorT(f)
  implicit def resultFromFutureValue[T](f: Future[T]): Result[T] = XorT(f.map(_.right[String]))

  implicit def fromXor[T](f: Xor[String, T]): ReaderResult[T] = ReaderT[Result, Config, T](_ => XorT(Future.successful(f)))
  implicit def fromResult[T](f: Result[T]): ReaderResult[T] = ReaderT[Result, Config, T](_ => f)

}
