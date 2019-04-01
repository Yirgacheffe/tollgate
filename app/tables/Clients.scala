//: tables: Clients.scala
package tables

import java.sql.{Date, Timestamp}

import slick.jdbc.MySQLProfile.api._


/**
  * Database entity related to table 'CLIENTS'
  *
  * @version 1.0.0 $ 2019-03-26 14:20 $
  */
class Clients( tag: Tag ) extends Table[Client]( tag, "CLIENTS" ) {


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