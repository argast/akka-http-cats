import com.tapad.docker.DockerComposePlugin.autoImport._
import com.typesafe.sbt.SbtNativePackager._
import sbt.Keys._
import sbt._

object Packaging {

  val packagingSettings = Seq(
    mainClass in Compile := Some("pfws.Main")

  )
}