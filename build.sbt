import CommonSettings._
import sbt.IntegrationTest

lazy val `akka-http-cats` = (project in file("."))
  .enablePlugins(GitVersioning, BuildInfoPlugin, JavaServerAppPackaging, DockerPlugin, GatlingPlugin)
  .configs(IntegrationTest)
  .settings(commonSettings: _*)
  .settings(Versioning.settings: _*)
  .settings(Packaging.settings: _*)
  .settings(BuildInfo.settings: _*)
  .settings(Testing.settings: _*)
  .settings(Dependencies.settings: _*)
  .settings(Gatling.settings: _*)
