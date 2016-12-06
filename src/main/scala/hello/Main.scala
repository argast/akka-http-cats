package hello

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Directives._
import hello.routes.{HelloRoutes, Info}
import org.slf4j.MDC

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object Main extends App {

  implicit val config = new Config() {
  }

  import config._

  val routes = captureRequestId {
    HelloRoutes.routes ~ Info.info
  }

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


  def captureRequestId = optionalHeaderValueByName("X-RequestId").map { requestId =>
    MDC.put("requestId", requestId.getOrElse(UUID.randomUUID().toString))
  }
}
