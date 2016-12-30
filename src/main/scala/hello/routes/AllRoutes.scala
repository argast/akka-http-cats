package hello.routes

import java.util.UUID

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.timeout.healthchecks.routes.HealthRoutes
import hello.Config
import hello.healthchecks.HelloChecks
import org.slf4j.MDC

import scala.language.postfixOps

object AllRoutes {

  def routes(implicit config: Config): Route = captureOrGenerateRequestId {
    import config.ec
    HelloRoutes.routes ~
    Info.info ~
    HealthRoutes.health(HelloChecks.all)
  }

  private def captureOrGenerateRequestId = optionalHeaderValueByName("X-RequestId").map { requestId =>
    MDC.put("requestId", requestId.getOrElse(UUID.randomUUID().getMostSignificantBits.toHexString))
  }
}
