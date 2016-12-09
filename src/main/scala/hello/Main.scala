package hello

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import hello.routes.AllRoutes

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object Main extends App {

  implicit val config = new Config()
  import config._

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
    Http().bindAndHandle(AllRoutes.routes, "0.0.0.0", port)
  }



}
