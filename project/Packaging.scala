import sbt.Keys._
import sbt._

object Packaging {

  val packagingSettings = Seq(
    mainClass in Compile := Some("pfws.Main")

  )
}