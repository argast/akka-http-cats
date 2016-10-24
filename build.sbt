import java.net.{InetSocketAddress, Socket, SocketAddress}

import CommonSettings._
import Dependencies._
import Packaging._
import Testing._
import DockerCompose._
import org.scalatest.concurrent.{Eventually, PatienceConfiguration}
import sbt.Keys._

import scala.concurrent.duration._
import org.scalatest.concurrent.PatienceConfiguration._

lazy val `akka-http-cats` = (project in file("."))
  .aggregate(`akka-http-cats-service`, `akka-http-cats-integration-tests`, `akka-http-cats-performance-tests`)
  .settings(commonSettings: _*)
  .settings(e2eSettings: _*)
  .dependsOn(`akka-http-cats-service`, `akka-http-cats-integration-tests`)

lazy val `akka-http-cats-service` = (project in file("akka-http-cats-service"))
  .enablePlugins(JavaServerAppPackaging, DockerPlugin)
  .settings(packagingSettings: _*)
  .settings(commonSettings: _*)
  .settings(serviceDependencies: _*)

def dockerHost = Option(System.getenv("DOCKER_HOST")).map(uri => new java.net.URI(uri).getHost).getOrElse("localhost")

lazy val `akka-http-cats-integration-tests` = (project in file("akka-http-cats-integration-tests"))
  .dependsOn(`akka-http-cats-service`)
  .settings(commonSettings: _*)
  .settings(integrationTestsDependencies: _*)
  .settings(dockerComposeSettings: _*)
  .settings(
    testOptions in Test := Seq(
      Tests.Setup( () => {
        Def.sequential(publishLocal in Docker in `akka-http-cats-service`, dockerComposeUp).value
      }),

      Tests.Cleanup(() => {
        dockerComposeDown.value
      })
    )
  )

lazy val `akka-http-cats-performance-tests` = (project in file("akka-http-cats-performance-tests"))
  .dependsOn(`akka-http-cats-service`)
  .settings(commonSettings: _*)
  .settings(performanceTestsDependencies: _*)

