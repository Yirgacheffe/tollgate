//: repository: AccessTokenRepository.scala
package repository

import java.sql.Timestamp
import javax.inject.{ Inject, Singleton }

import scala.concurrent.{ ExecutionContext, Future }

import play.api.Logger
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import tables.{ AccessTokens, AccessToken }

import tables.YesNoBoolean
import tables.YesNoBoolean._


/**
  * Data access layer repository for 'AccessToken'
  *
  * @version 1.0.0 $ 2019-03-27 15:26 $
  */
@Singleton
class AccessTokenRepository @Inject()( dbConfigProvider: DatabaseConfigProvider )( implicit ec: ExecutionContext ) {


  private val logger: Logger = Logger( this.getClass )

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val accessTokens = TableQuery[AccessTokens]


  /**
    * Create access token for current login
    */
  def create( token: String, issuedAt: Timestamp, expiredAfter: Timestamp, isExpired: YesNoBoolean,
              clientId: Int ): Future[Int] = db.run {

    accessTokens.map(t =>
      ( t.token, t.issuedAt, t.expiredAfter, t.isExpired, t.clientId) ) += ( token, issuedAt, expiredAfter, isExpired, clientId )

  }


  /**
    *
    */
  def findExistTokenByClientId( id: Int ): Future[Option[AccessToken]] = {

    val q = for (
      t <- accessTokens
      if t.clientId === id && t.isExpired === No.asInstanceOf[YesNoBoolean]
    ) yield ( t )

    db.run {
      q.sortBy( _.id.desc ).result.headOption
    }

  }


} //:~
