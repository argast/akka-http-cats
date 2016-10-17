import sbt.Keys._
import sbt._

object Dependencies {

  val akkaVersion = "2.4.10"
  val catsVersion = "0.7.2"

  val commonDependencies = Seq(
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "org.slf4j" % "slf4j-api" % "1.7.21",
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "org.typelevel" %% "cats" % catsVersion
  )

  val serviceDependencies = deps(
    commonDependencies ++ Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "io.circe" %% "circe-core" % "0.5.0-M3",
      "io.circe" %% "circe-generic" % "0.5.0-M3",
      "io.circe" %% "circe-parser" % "0.5.0-M3",
      "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % "test"
    ))

  val integrationTestsDependencies = deps(
    commonDependencies ++ Seq(
      "com.jayway.restassured" % "rest-assured" % "2.8.0"
    ))

  val performanceTestsDependencies = deps(
    commonDependencies ++ Seq(
      "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.1.7"
    ))

  private def deps(modules: Seq[ModuleID]): Seq[Setting[_]] = Seq(libraryDependencies ++= modules)
}