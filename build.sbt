import java.net.{InetSocketAddress, Socket, SocketAddress}

import CommonSettings._
import Dependencies._
import Packaging._
import Testing._
import org.scalatest.concurrent.{Eventually, PatienceConfiguration}
import sbt.Keys._

import scala.concurrent.duration._
import org.scalatest.concurrent.PatienceConfiguration._

lazy val `cats-reader` = (project in file("."))
  .aggregate(`cats-reader-service`, `cats-reader-integration-tests`, `cats-reader-performance-tests`)
  .settings(commonSettings: _*)
  .settings(e2eSettings: _*)
  .enablePlugins(DockerComposePlugin)
  .dependsOn(`cats-reader-service`, `cats-reader-integration-tests`)

lazy val `cats-reader-service` = (project in file("cats-reader-service"))
  .enablePlugins(JavaServerAppPackaging, DockerPlugin)
  .settings(packagingSettings: _*)
  .settings(commonSettings: _*)
  .settings(serviceDependencies: _*)

def dockerHost = Option(System.getenv("DOCKER_HOST")).map(uri => new java.net.URI(uri).getHost).getOrElse("localhost")

lazy val `cats-reader-integration-tests` = (project in file("cats-reader-integration-tests"))
  .dependsOn(`cats-reader-service`)
  .settings(commonSettings: _*)
  .settings(integrationTestsDependencies: _*)
  .settings(
    testOptions in Test := Seq(
      Tests.Setup( () => {
        (publishLocal in Docker in `cats-reader-service`).value
        println("Starting docker-compose")
        val currentVersion = (version in `cats-reader-service`).value
        Process(Seq("docker-compose", "up", "-d"), None, "VERSION" -> currentVersion).!
        Eventually.eventually(PatienceConfiguration.Timeout(10 seconds), PatienceConfiguration.Interval(1 second)) {
          new Socket().connect(new InetSocketAddress("192.168.99.100", 8090), 250)
        }
      }),

      Tests.Cleanup(() => {
        println("Stopping docker-compose")
        val currentVersion = (version in `cats-reader-service`).value
        Process(Seq("docker-compose", "down"), None, "VERSION" -> currentVersion).!
      })
    )
  )

lazy val `cats-reader-performance-tests` = (project in file("cats-reader-performance-tests"))
  .dependsOn(`cats-reader-service`)
  .settings(commonSettings: _*)
  .settings(performanceTestsDependencies: _*)
