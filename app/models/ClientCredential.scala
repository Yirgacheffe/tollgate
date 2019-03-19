//: models: ClientCredential.scala
package models

/**
  * Class for verify the client credential from http request
  *
  * @version 1.0 $ 2019-03-19 15:27 $
  */
case class ClientCredential( clientId: String, clientSecret: String )
