//: repository: AccessTokenRepository.scala
package repository

import java.time.LocalDateTime
import java.sql.Timestamp

import javax.inject.{ Inject, Singleton }

import scala.concurrent.{ ExecutionContext, Future }
import play.api.Logger
import play.api.db.slick.DatabaseConfigProvider

import slick.jdbc.JdbcProfile

import tables.{ AccessToken, AccessTokens }
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
  private val writeTokens  = accessTokens returning accessTokens.map( _.id ) into ( (token, id) => token.copy( id = id) )


  private def toTS( dt: LocalDateTime ): Timestamp = Timestamp.valueOf( dt )


  /**
    * Get token by primary key
    */
  def findById( id: Int ): Future[Option[AccessToken]] = db.run {
    accessTokens.filter( _.id === id).result.headOption
  }


  def create( token: String, issuedAt: LocalDateTime, expiredAfter: LocalDateTime,
              isExpired: YesNoBoolean,
              clientId: Int ): Future[AccessToken] = db.run {

    writeTokens  += AccessToken( token, toTS( issuedAt ), toTS( expiredAfter ), isExpired, clientId )

  }


  def findExistTokenByClientId( id: Int ): Future[Option[AccessToken]] = {

    val q = for (
      t <- accessTokens
      if t.clientId === id && t.isExpired === No.asInstanceOf[YesNoBoolean]
    ) yield t

    db.run {
      q.sortBy( _.id.desc ).result.headOption  // Get latest access token, ideally should be only 1 exist
    }

  }


} //:~
