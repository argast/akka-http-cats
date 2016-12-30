package hello.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.StrictLogging
import de.heikoseeberger.akkahttpcirce.CirceSupport
import pfws.BuildInfo

object Info extends StrictLogging with CirceSupport {
  def info: Route = pathSingleSlash {
    complete {
      Thread.sleep(1000)
      BuildInfo.toMap.mapValues(_.toString) + (
        "health" -> "http://localhost:8090/health"
      )
    }
  }
}