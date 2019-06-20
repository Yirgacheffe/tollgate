//: tables: User.scala
package tables

import java.sql.Date


/**
  * Database entity related to table 'USERS'
  *
  * @version 1.0 $ 2019-06-20 14:03 $
  */
case class User( username: String, password: String, effectiveFrom: Date, expiredAfter: Date, id: Int = 0 )
