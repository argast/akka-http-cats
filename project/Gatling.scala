import sbt.Keys._
import sbt._

object Gatling {

  val settings = Seq(
    scalaSource in io.gatling.sbt.GatlingKeys.Gatling := sourceDirectory.value / "gatling" / "scala",
    fullClasspath in io.gatling.sbt.GatlingKeys.Gatling += crossTarget.value / "gatling-classes"
  )
}
