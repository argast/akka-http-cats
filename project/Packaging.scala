import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import com.typesafe.sbt.packager.docker._

object Packaging {

  val settings = Seq(
    dockerBaseImage := "anapsix/alpine-java:8",
    dockerRepository := Some("argast"),
    dockerBuildOptions ++= Seq("-t", dockerAlias.value.copy(tag = Some("local")).versioned),
    dockerCommands += Cmd("HEALTHCHECK", "CMD", "wget", "-s", "http://localhost:8090/health")
  )
}