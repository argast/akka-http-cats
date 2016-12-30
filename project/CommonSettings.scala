import sbt.Keys.{resolvers, _}
import sbt.Resolver

object CommonSettings {

  val commonSettings = Seq(
    scalaVersion := "2.12.1",
    name := "akka-http-cats",
    resolvers ++= Seq(
      Resolver.bintrayRepo("argast","maven"),
      Resolver.sonatypeRepo("snapshots")
    )
  )
}