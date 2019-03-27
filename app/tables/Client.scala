//: models: Client.scala
package tables

import java.sql.{Date, Timestamp}
import java.time.{Instant, OffsetDateTime, ZoneId}

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


  def toDT( ts: Timestamp ): OffsetDateTime = OffsetDateTime.ofInstant( Instant.ofEpochMilli(ts.getTime), ZoneId.systemDefault() )
  def toTS( dt: OffsetDateTime ): Timestamp = new Timestamp( dt.getNano )

  implicit val tsFormat = new Format[Timestamp] {

    def writes( ts: Timestamp ): JsValue = Json.toJson( toDT(ts) )
    def reads(  json: JsValue ): JsResult[Timestamp] = Json.fromJson[OffsetDateTime](json).map( toTS )

  }

  implicit val clientFormat = Json.format[Client]


} //:~
