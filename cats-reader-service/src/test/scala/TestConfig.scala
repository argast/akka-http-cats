import akka.actor.ActorSystem
import pfws.Config

class TestConfig extends Config {

  override val database: Map[String, String] = Map("separator" -> "| ")
}
