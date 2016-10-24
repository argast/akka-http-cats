import java.net.{InetSocketAddress, Socket}

import org.scalatest.concurrent.{Eventually, PatienceConfiguration}
import sbt.Keys._
import sbt._

import scala.concurrent.duration._

object DockerCompose {

  lazy val dockerComposeUp = taskKey[Unit]("docker-compose up.")

  lazy val dockerComposeDown = taskKey[Unit]("docker-compose down.")

  def dockerHost = Option(System.getenv("DOCKER_HOST")).map(uri => new java.net.URI(uri).getHost).getOrElse("localhost")

  val dockerComposeSettings = Seq(

    dockerComposeUp := {
      println("Starting docker-compose")
      val currentVersion = version.value
      Process(Seq("docker-compose", "up", "-d"), None, "VERSION" -> currentVersion).!
      Eventually.eventually(PatienceConfiguration.Timeout(10 seconds), PatienceConfiguration.Interval(1 second)) {
        new Socket().connect(new InetSocketAddress(dockerHost, 8090), 250)
      }
    },

    dockerComposeDown := {
      println("Stopping docker-compose")
      val currentVersion = version.value
      Process(Seq("docker-compose", "down"), None, "VERSION" -> currentVersion).!
    }
  )
}