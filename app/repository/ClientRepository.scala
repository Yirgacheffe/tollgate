//: repository: ClientRepository.scala
package repository

import javax.inject.{Inject, Singleton}

import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import models.ClientCredential
import tables.{ Client, Clients }


/**
  * Data access layer repository for 'Client'
  *
  * @version 1.0.0 $ 2019-03-27 15:25 $
  */
@Singleton
class ClientRepository @Inject()(dbConfigProvider: DatabaseConfigProvider )(implicit ec: ExecutionContext ) {


  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val clients = TableQuery[Clients]


  /**
    * Get client account by using client credential
    *
    * @params client id and secret
    */
  def findByClientCredential( maybeCredential: ClientCredential ): Future[Option[Client]] = {

    val clientId = maybeCredential.clientId
    val secret   = maybeCredential.clientSecret

    val q = for {
      c <- clients
      if c.clientId === clientId && c.secret === secret
    } yield c

    db.run {
      q.result.headOption     // Validate client account, should be only 1 record existing
    }

  }


} //:~
