//: repository: UserRepository.scala
package repository

import javax.inject.{ Inject, Singleton }

import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.DatabaseConfigProvider



/**
  * Data access layer repository for 'User'
  *
  * @version 1.0.0 $ 2019-06-20 14:18 $
  */
@Singleton
class UserRepository @Inject()( dbConfigProvider: DatabaseConfigProvider )(implicit ec: ExecutionContext)  {



} //:~
