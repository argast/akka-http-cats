import sbt.Keys._
import sbt._
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._

object Packaging {

  val packagingSettings = Seq(
    dockerBaseImage := "anapsix/alpine-java:8",
    dockerRepository := Some("argast")
  )
}