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
import tables.YesNoBoolean
import tables.AccessToken


/**
  * OAuth2Controller handle all restful apis for oauth grant flow
  *
  * @version 1.0 $ 2019-03-18 17:19 $
  */
class OAuth2Controller @Inject() ( cc: ControllerComponents, clientRepo: ClientRepository, tokenRepo: AccessTokenRepository )( implicit ec: ExecutionContext )
    extends AbstractController( cc ) {


  private val logger: Logger = Logger( this.getClass )


  case class OAuth2Response( tokenType: String = "bearer", expiresIn: Int = 3600, accessToken: String, refreshToken: Option[String], scope: Option[String] )


  private val bad_request_caused_by_invalid_parameter =
      Status(400)(
          Json.obj("message" -> "Invalid request parameters.", "severity" -> "WARN") )


  private def clientCredentialResponse( token: String, expiredId: Int = 3600 ) : String = {
    Json.obj(
      "token_type" -> "bearer", "access_token" -> token, "expires_in" -> expiredId, "scope" -> "none" ).toString()
  }


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
        case Some( c ) => {

          val x = issueAccessTokenByCorrectCredential( c.id ).map {
            t => t
          }



          Ok()

        }
        case None      => NotFound( Json.obj("message" -> "Client not found.", "severity" -> "ERROR") )
      }

    }

  }


  private def createAccessToken( clientId: Int, expiredIn: Int = 3600 ): Future[AccessToken] = {

    import java.sql.Timestamp

    def toTS( dt: LocalDateTime ): Timestamp = Timestamp.valueOf( dt )
    def generateToken = UUID.randomUUID().toString

    val issuedAt     = LocalDateTime.now()
    val expiredAfter = issuedAt.plusSeconds( expiredIn )

    tokenRepo.create( generateToken, toTS( issuedAt ), toTS( expiredAfter ), YesNoBoolean.No, clientId )

  }



  private def issueAccessTokenByCorrectCredential( clientId: Int ): Future[AccessToken] = {


    tokenRepo.findExistTokenByClientId( clientId ).flatMap {
      case Some( token ) => Future.successful( token )
      case None          => createAccessToken( clientId )
    }

    // If has exist token then return the current token, else create a new one for the client

  }


} //:~
