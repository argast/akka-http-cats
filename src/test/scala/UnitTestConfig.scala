import pfws.Config

class UnitTestConfig extends Config {

  override val database: Map[String, String] = Map("separator" -> "| ")
}
