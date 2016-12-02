import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Directives._
import pfws.{Config, HelloRoutes}
import routes.Info

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
import scala.language.postfixOps

object Main extends App {

  implicit val config = new Config {
    implicit val ec = ExecutionContext.Implicits.global
  }

  import config._

  val routes = HelloRoutes.routes ~ Info.info

  val server = Await.result(startServer, 5 seconds)

  sys.addShutdownHook {
    Await.ready(stopServer(server), 5 seconds)
  }

  def stopServer(server: ServerBinding)(implicit s: ActorSystem) = {
    for {
      _ <- server.unbind()
      _ <- s.terminate()
    } yield ()
  }

  def startServer = {
    Http().bindAndHandle(routes, "0.0.0.0", port)
  }
}
