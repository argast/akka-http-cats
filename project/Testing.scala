import sbt.Keys.{testOptions, _}
import sbt._
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import io.gatling.sbt.GatlingPlugin.autoImport._

object Testing {

  def isLocalTest = System.getProperty("test.against", "local") == "local"

  private val testSetupTask = Def.taskDyn {
    if (isLocalTest) {
      Def.task {
        (publishLocal in Docker).value
        DockerCompose.up(streams.value.log, version.value)
      }
    } else Def.task(())
  }

  private val testCleanupTask = Def.taskDyn {
    if (isLocalTest) Def.task(DockerCompose.down(streams.value.log, version.value))
    else Def.task(())
  }

  val settings = Seq(
    testOptions in IntegrationTest := Seq(
      Tests.Setup( () => testSetupTask.value),
      Tests.Cleanup( () => testCleanupTask.value)
    ),
      testOptions in io.gatling.sbt.GatlingKeys.Gatling := Seq(
      Tests.Setup( () => testSetupTask.value),
      Tests.Cleanup( () => testCleanupTask.value)
    )
  ) ++ Defaults.itSettings
}