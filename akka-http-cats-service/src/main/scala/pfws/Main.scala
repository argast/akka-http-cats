package pfws

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
import cats.instances.all._
import cats.syntax.all._

import scala.language.postfixOps

object Main extends App {


  implicit val config = new Config {
  }

  import config._

  implicit val ec = ExecutionContext.Implicits.global

  val routes = HelloRoutes.routes

  val server = Await.result(startServer, 5 seconds)

  sys.addShutdownHook {
    Await.ready(stopServer(server), 5 seconds)
  }

  def stopServer(server: ServerBinding)(implicit s: ActorSystem) = server.unbind() >>= { _ => s.terminate() }

  def startServer = {
    Http().bindAndHandle(routes, "0.0.0.0", port)
  }
}
