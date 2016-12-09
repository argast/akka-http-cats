package hello.routes

import java.util.UUID

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import hello.Config
import org.slf4j.MDC

import scala.language.postfixOps

object AllRoutes {

  def routes(implicit config: Config): Route = captureRequestId {
    HelloRoutes.routes ~
    Info.info
  }

  private def captureRequestId = optionalHeaderValueByName("X-RequestId").map { requestId =>
    MDC.put("requestId", requestId.getOrElse(UUID.randomUUID().getMostSignificantBits.toHexString))
  }
}
