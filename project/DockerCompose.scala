import java.net.{URI => _, _}

import org.scalatest.concurrent.{Eventually, PatienceConfiguration}
import sbt._

import scala.collection.convert.DecorateAsScala
import scala.concurrent.duration._

object DockerCompose extends DecorateAsScala {

  def getLocalIpAddress = {
    val ip4Address: PartialFunction[InetAddress, String] = { case a: Inet4Address => a.getHostAddress }
    val interfaces = NetworkInterface.getNetworkInterfaces.asScala.toSeq
    val interface = interfaces.find(i => i.getName == "en0" || i.getName == "eth0")
    interface.flatMap(_.getInetAddresses.asScala.toSeq.collect(ip4Address).headOption).getOrElse(InetAddress.getLocalHost.getHostAddress)
  }

  def dockerMachineIp =
    Option(System.getenv("DOCKER_HOST")).map(h => new URI(h).getHost) // docker-machine setup
      .getOrElse(getLocalIpAddress) // docker for mac setup

  def up(log: Logger, version: String) = {
    Process(Seq("docker-compose", "up", "-d"), None, "VERSION" -> version).!!(log)
    Eventually.eventually(PatienceConfiguration.Timeout(10 seconds), PatienceConfiguration.Interval(1 second)) {
      log.info(s"Waiting for service to be ready on $dockerMachineIp:8090.")
      val c = new URL(s"http://$dockerMachineIp:8090/hello").openConnection().asInstanceOf[HttpURLConnection]
      assert(c.getResponseCode == 200)
    }
    log.info(s"Service ready.")
  }

  def down(log: Logger, version: String) = {
    log.info("Stopping docker-compose")
    Process(Seq("docker-compose", "down"), None, "VERSION" -> version).!!(log)
  }
}