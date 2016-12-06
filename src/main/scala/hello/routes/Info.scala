package hello.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.StrictLogging
import pfws.BuildInfo

object Info extends StrictLogging {
  def info: Route = pathSingleSlash {
    complete {
      BuildInfo.toJson
    }
  }
}
