import com.typesafe.sbt.GitPlugin.autoImport._
import sbt.Keys._
import sbtbuildinfo.BuildInfoPlugin.autoImport._

object BuildInfo {

  val settings = Seq(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "pfws",
    buildInfoUsePackageAsPath := true,
    buildInfoOptions += BuildInfoOption.ToJson
  )
}