import sbt.Keys._
import sbt._

object Dependencies {

  val akkaVersion = "2.4.14"
  val akkaHttpVersion = "10.0.0"
  val catsVersion = "0.8.1"

  val settings = deps(
    Seq(
      "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.3" % "test",
      "io.gatling"            % "gatling-test-framework"    % "2.2.3" % "test",
      "ch.qos.logback" % "logback-classic" % "1.1.7",
      "org.slf4j" % "slf4j-api" % "1.7.21",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
      "org.typelevel" %% "cats" % catsVersion,
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "io.circe" %% "circe-core" % "0.6.1",
      "io.circe" %% "circe-generic" % "0.6.1",
      "io.circe" %% "circe-parser" % "0.6.1",
      "de.heikoseeberger" %% "akka-http-circe" % "1.11.0",
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test,it"
    ))


  private def deps(modules: Seq[ModuleID]): Seq[Setting[_]] = Seq(libraryDependencies ++= modules)
}