import sbt.Keys._
import sbt._

object Testing {

  lazy val hello = taskKey[Unit]("Prints 'Hello World'")

  val e2eSettings = Seq(
    hello := println("hello world!")
  )
}