package pfws

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

trait Config {

  val port = 8090

  implicit val actorSystem = ActorSystem("hello-reader")
  implicit val actorMaterialiser = ActorMaterializer()

  val database = Map[String, String]("separator" -> ", ")

  lazy val pipeline = Http()

  val greetingUrl = "http://localhost:9090"
}
