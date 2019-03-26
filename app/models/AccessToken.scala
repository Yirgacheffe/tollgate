//: models: AccessToken.scala
package models

import java.sql.Timestamp


/**
  * Database entity related to table 'ACCESS_TOKENS'
  *
  * @version 1.0 $ 2019-03-18 17:02 $
  */
case class AccessToken( id: Int, token: String, issuedAt: Timestamp, expiredAfter: Timestamp, isExpired: Boolean,
                        createdAt: Timestamp,
                        updatedAt: Timestamp )
