package hello

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

class Config {

  val port = 8090

  implicit val actorSystem = ActorSystem("hello-reader")
  implicit val actorMaterializer = ActorMaterializer()
  implicit val ec = actorSystem.dispatcher
}
