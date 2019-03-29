//: tables: AccessTokens.scala
package tables

import java.sql.Timestamp

import slick.jdbc.MySQLProfile.api._


/**
  * Database entity related to table 'ACCESS_TOKENS'
  *
  * @version 1.0.0 $ 2019-03-26 14:28 $
  */
class AccessTokens( tag: Tag ) extends Table[AccessToken]( tag, "ACCESS_TOKENS") {

  def id           = column[Int]( "ID", O.PrimaryKey, O.AutoInc )
  def token        = column[String]      ( "TOKEN"         )

  def issuedAt     = column[Timestamp]   ( "ISSUED_AT"     )
  def expiredAfter = column[Timestamp]   ( "EXPIRED_AFTER" )

  def isExpired    = column[YesNoBoolean]( "IS_EXPIRED"    )
  def clientId     = column[Int]         ( "ID_CLIENT"     )

  def createdAt    = column[Timestamp]   ( "CREATED_AT"    )
  def updatedAt    = column[Timestamp]   ( "UPDATED_AT"    )

  def * = ( id, token, issuedAt,
    expiredAfter,
    isExpired,
    clientId,
    createdAt, updatedAt ) <> ( (AccessToken.apply _).tupled, AccessToken.unapply )


} //:~
