import CommonSettings._
import Dependencies._
import Packaging._
import sbt.Keys._
import sbt.Tests

lazy val `akka-http-cats` = (project in file("."))
  .aggregate(`akka-http-cats-service`, `akka-http-cats-integration-tests`, `akka-http-cats-performance-tests`)
  .settings(commonSettings: _*)
  .dependsOn(`akka-http-cats-service`, `akka-http-cats-integration-tests`)

lazy val `akka-http-cats-service` = (project in file("akka-http-cats-service"))
  .enablePlugins(JavaServerAppPackaging, DockerPlugin)
  .settings(packagingSettings: _*)
  .settings(commonSettings: _*)
  .settings(serviceDependencies: _*)

lazy val `akka-http-cats-integration-tests` = (project in file("akka-http-cats-integration-tests"))
  .dependsOn(`akka-http-cats-service`)
  .settings(commonSettings: _*)
  .settings(integrationTestsDependencies: _*)
  .settings(
    testOptions in Test := Seq(
      Tests.Setup(() => {
        (publishLocal in Docker in `akka-http-cats-service`).value
        DockerCompose.up(streams.value.log, version.value)
      }),
      Tests.Cleanup(() => {
        DockerCompose.down(streams.value.log, version.value)
      })
    )
  )

lazy val `akka-http-cats-performance-tests` = (project in file("akka-http-cats-performance-tests"))
  .enablePlugins(GatlingPlugin)
  .settings(commonSettings: _*)
    .settings(
      // until gatling supports scala 2.12
      scalaVersion := "2.11.8"
    )
  .settings(performanceTestsDependencies: _*)
  .settings(
    testOptions in Gatling := Seq(
      Tests.Setup(() => {
        (publishLocal in Docker in `akka-http-cats-service`).value
        DockerCompose.up(streams.value.log, version.value)
      }),
      Tests.Cleanup(() => {
        DockerCompose.down(streams.value.log, version.value)
      })
    )
  )

