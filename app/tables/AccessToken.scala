//: tables: AccessToken.scala
package tables

import java.sql.Timestamp


/**
  * Database entity related to table 'ACCESS_TOKENS'
  *
  * @version 1.0 $ 2019-03-18 17:02 $
  */
case class AccessToken( token: String, issuedAt: Timestamp, expiredAfter: Timestamp, isExpired: YesNoBoolean,
                        clientId: Int, id: Int = 0 )
