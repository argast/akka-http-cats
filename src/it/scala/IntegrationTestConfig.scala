import com.typesafe.config.ConfigFactory

object IntegrationTestConfig {

  private val config = ConfigFactory.load(System.getProperty("test.against", "local") + ".conf")

  val hostname = config.getString("service.hostname")
}
