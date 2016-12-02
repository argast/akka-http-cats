import com.typesafe.sbt.GitPlugin.autoImport._
import sbt.Keys._
import sbtbuildinfo.BuildInfoPlugin.autoImport._

object Versioning {

  val settings = Seq(
    git.useGitDescribe := true
  )
}