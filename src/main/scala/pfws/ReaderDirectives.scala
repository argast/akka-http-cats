package pfws

import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

import language.higherKinds
import cats.instances.all._
import cats.syntax.all._
import cats.data._

object ReaderDirectives {

  def completeReader[F[_], T](reader: ReaderT[F, Config, T])(implicit m: ToResponseMarshaller[F[T]], c: Config): Route = complete {
    reader(c)
  }

}
