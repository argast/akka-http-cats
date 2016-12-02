package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import pfws.BuildInfo

object Info {
  def info: Route = pathSingleSlash {
    complete {
      BuildInfo.toJson
    }
  }
}
