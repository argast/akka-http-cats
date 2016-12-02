import sbt.Keys.{resolvers, _}
import sbt.Resolver

object CommonSettings {

  val commonSettings = Seq(
    scalaVersion := "2.11.8",
    resolvers += Resolver.bintrayRepo("timeoutdigital","releases")
  )
}