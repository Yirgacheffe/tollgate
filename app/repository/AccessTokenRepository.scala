//: repository: AccessTokenRepository.scala
package repository

import java.sql.Timestamp

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import models.AccessToken
import tables.AccessTokens


@Singleton
class AccessTokenRepository @Inject()( dbConfigProvider: DatabaseConfigProvider )( implicit ec: ExecutionContext ) {


  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private val accessTokens = TableQuery[AccessToken]


  /**
    *
    */
  def create( token: String, issuedAt: Timestamp, expiredAfter: Timestamp,
              isExpired: Boolean ): Future[Int] = db.run {

    accessTokens.map ( t =>
      ( t.token, t.issuedAt, t.expiredAfter, t.isExpired ) ) += ( token, issuedAt, expiredAfter, isExpired )

  }


} //:~
