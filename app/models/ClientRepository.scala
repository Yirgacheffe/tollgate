//: models: ClientRepository.scala
package models

import java.sql.{ Date, Timestamp }
import javax.inject.{ Inject, Singleton }

import scala.concurrent.{ ExecutionContext, Future }

import slick.jdbc.JdbcProfile
import play.api.db.slick.DatabaseConfigProvider


@Singleton
class ClientRepository @Inject()(dbConfigProvider: DatabaseConfigProvider )(implicit ec: ExecutionContext ) {


  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class Clients( tag: Tag ) extends Table[Client]( tag, "CLIENTS" ) {

    def id       = column[Int]( "ID", O.PrimaryKey, O.AutoInc )

    def clientId = column[String]( "CLIENT_ID" )
    def secret   = column[String]( "SECRET" )
    def name     = column[String]( "NAME"   )

    def effectiveFrom = column[Date]( "EFFECTIVE_FROM"  )
    def expiredAfter  = column[Date]( "EXPIRED_AFTER"   )
    def createdAt     = column[Timestamp]( "CREATED_AT" )
    def updatedAt     = column[Timestamp]( "UPDATED_AT" )

    def * = ( id, clientId, secret,
      name,
      effectiveFrom,
      expiredAfter,
      createdAt, updatedAt ) <> ( (Client.apply _).tupled, Client.unapply )

  } //:~


  private val clients = TableQuery[Clients]


  def list(): Future[Seq[Client]] = db.run { clients.result }

  def findClientCredential( maybeCredential: ClientCredential ): Future[Option[Client]] = {

    val clientId = maybeCredential.clientId
    val secret   = maybeCredential.clientSecret

    val q = for {
      c <- clients
      if c.clientId === clientId && c.secret === secret
    } yield c

    db.run {
      q.result.headOption     // Validate client account, should be only 1 record
    }

  }


} //:~
