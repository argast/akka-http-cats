import com.typesafe.sbt.GitPlugin.autoImport._
import sbt.Keys._
import sbtbuildinfo.BuildInfoPlugin.autoImport._

object Versioning {

  val settings = Seq(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "akka-http-cats",
    buildInfoUsePackageAsPath := true,
    buildInfoOptions += BuildInfoOption.ToJson,
    git.useGitDescribe := true
  )
}