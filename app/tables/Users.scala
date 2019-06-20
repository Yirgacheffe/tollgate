//: table: Users.scala
package tables

import java.sql.Date
import slick.jdbc.MySQLProfile.api._


/**
  * Database entity related to table 'USERS'
  *
  * @version 1.0.0 $ 2019-03-26 14:28 $
  */
case class Users( tag: Tag ) extends Table[User]( tag, "Users" ) {


  def id       = column[Int]( "ID", O.PrimaryKey, O.AutoInc )
  def username = column[String]( "USER_NAME" )
  def password = column[String]( "PASSWORD" )

  def effectiveFrom = column[Date]( "EFFECTIVE_FROM"  )
  def expiredAfter  = column[Date]( "EXPIRED_AFTER"   )


  def * = ( username, password, effectiveFrom,
    expiredAfter,
    id ) <> ( (User.apply _).tupled, User.unapply )


} //:~
