//: repository: AccessTokenRepository.scala
package repository

import java.sql.Timestamp

import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import tables.AccessToken
import tables.{ AccessTokens, YesNoBoolean }


/**
  * Data access layer repository for 'AccessToken'
  *
  * @version 1.0.0 $ 2019-03-27 15:26 $
  */
@Singleton
class AccessTokenRepository @Inject()( dbConfigProvider: DatabaseConfigProvider )( implicit ec: ExecutionContext ) {


  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private val accessTokens = TableQuery[AccessTokens]


  /**
    *
    */
  def create( token: String, issuedAt: Timestamp, expiredAfter: Timestamp,
              isExpired: Boolean ): Future[AccessToken] = db.run {

    (
      accessTokens.map( t =>
        ( t.token, t.issuedAt, t.expiredAfter, t.isExpired )
      ) returning
          accessTokens.map( _.id )
        into {
          (accessToken, id ) => accessToken.copy( id = id )
        }
    ) += ( token, issuedAt, expiredAfter, YesNoBoolean.fromBool(isExpired) )

  }


} //:~
