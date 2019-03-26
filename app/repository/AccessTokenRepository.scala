//: repository: AccessTokenRepository.scala
package repository

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.ExecutionContext


@Singleton
class AccessTokenRepository @Inject()( dbConfigProvider: DatabaseConfigProvider )(implicit ec: ExecutionContext ) {





} //:~
