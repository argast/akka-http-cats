package pfws

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import cats.instances.all._
import cats.syntax.all._
import cats.data._

object Database {

  def get(key: String): ReaderResult[String] =  ReaderT { config =>
    config.database(key)
  }

  def getDefault(key: String, default: String): ReaderT[Future, Config, String] = ReaderT { config =>
    Future {
      config.database.getOrElse(key, default)
    }
  }


}
