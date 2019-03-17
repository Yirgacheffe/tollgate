//: models: Client.scala
package models

import java.sql.Date
import java.sql.Timestamp

import play.api.libs.json._


/**
  * Database entity related to table 'CLIENTS'
  *
  * @version 1.0 $ 2019-03-15 13:58 $
  */
case class Client( id: Int, clientId: String, secret: String, name: String,
                   effectiveFrom: Date,
                   expiredAfter : Date,
                   createdAt: Timestamp, updatedAt: Timestamp )

object Client {
  implicit val clientFormat = Json.format[Client]
}
