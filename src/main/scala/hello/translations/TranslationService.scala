package hello.translations

import cats.data.ReaderT
import hello.result._
import hello.result.implicits._

trait Translations {

  def translate(language: String): Result[Option[String]]

}

class RedisTranslations extends Translations {

  override def translate(language: String): Result[Option[String]] = ReaderT { config =>
    import config._
    translationsMap.get(language)
  }
}
