//: controllers: OAuth2Controller.scala
package controllers

import java.time.Instant
import java.util.UUID

import javax.inject._

import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc._
import play.api.Logger
import play.api.libs.json._
import models._
import repository._


/**
  * OAuth2Controller handle all restful apis for oauth grant flow
  *
  * @version 1.0 $ 2019-03-18 17:19 $
  */
class OAuth2Controller @Inject() ( cc: ControllerComponents, clientRepo: ClientRepository, tokenRepo: AccessTokenRepository )( implicit ec: ExecutionContext )
    extends AbstractController( cc ) {


  private val logger: Logger = Logger( this.getClass )


  case class OAuth2Response( tokenType: String = "bearer", expiresIn: Int = 3600, accessToken: String,
                             refreshToken: Option[String] )


  private val bad_request_caused_by_invalid_parameter =
      Status(400)(
        Json.obj("message" -> "Invalid request parameters.", "severity" -> "WARN") )


  /**
    *
    */
  def token() = Action.async { implicit request =>

    val body   = request.body
    val params = body.asFormUrlEncoded.getOrElse( Map.empty[String, Seq[String]] )

    def param( name: String ): Option[String] = params.get( name ).flatMap( values => values.headOption )
    def requiredParam( name: String ): String = param( name ).getOrElse( "" )

    val grantType    = requiredParam( "grant_type" )
    val clientId     = requiredParam( "client_id"  )
    val clientSecret = requiredParam( "client_secret" )


    if ( grantType.isEmpty || !grantType.equals("client_credentials")
                           || clientId.isEmpty
                           || clientSecret.isEmpty ) {

      logger.info( "Invalid request parameters found." )

      Future.successful(
        bad_request_caused_by_invalid_parameter
      )

    } else {

      clientRepo.findByClientCredential( ClientCredential( clientId, clientSecret ) ).map {
        case Some(c) => Ok( xyzdfd() )
        case None    => NotFound( Json.obj("message" -> "Client not found.", "severity" -> "ERROR") )
      }

    }

  }








  private def xyzdfd() : String = {


    val issuedAt  = Instant.now()
    val expiredIn = 3600

    // val expiredAfter = issuedAt. + expiredIn

    // tokenRepo.create( UUID.randomUUID().toString, Instant.now, Instant.now, False )

    Json.obj(
      "token_type" -> "bearer", "access_token" -> UUID.randomUUID().toString,  "expires_in" -> 3600 ).toString()

  }


} //:~
