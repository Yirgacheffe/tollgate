//: controllers: OAuth2Controller.scala
package controllers

import java.time.LocalDateTime
import java.util.UUID

import javax.inject._

import scala.concurrent.{ ExecutionContext, Future }
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
        case Some(c) =>
          {
            logger.info( c.name )
            Ok( xyzdfd() )
          }
        case None    => NotFound( Json.obj("message" -> "Client not found.", "severity" -> "ERROR") )
      }

    }

  }



  private def xyzdfd() : String = {


    import java.sql.Timestamp
    def toTS( dt: LocalDateTime ): Timestamp = new Timestamp( dt.getNano )


    val issuedAt  = LocalDateTime.now()
    val expiredIn = 3600
    val expiredAfter = issuedAt.plusSeconds( expiredIn )
    val rdmToken = UUID.randomUUID().toString

    logger.info( rdmToken )

    val syz = tokenRepo.create( rdmToken, toTS( issuedAt ), toTS( expiredAfter ), false )

    syz.map { c =>
      logger.info( c.toString )
    }

    Json.obj(
      "token_type" -> "bearer", "access_token" -> rdmToken, "expires_in" -> expiredIn, "scope" -> "none" ).toString()

  }


} //:~
