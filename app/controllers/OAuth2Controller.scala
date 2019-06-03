//: controllers: OAuth2Controller.scala
package controllers

import java.time.LocalDateTime
import java.util.UUID

import javax.inject._

import scala.concurrent.{ ExecutionContext, Future }
import play.api.mvc._
import play.api.Logger
import play.api.libs.json._

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
        Status(400)(
          Json.obj("message" -> "Invalid request parameters.", "severity" -> "WARN" )
        )
      )

    } else {

      clientRepo.findByClientCredential( clientId, clientSecret ).flatMap {
        case Some( c ) => {
          issueAccessTokenByCorrectCredential( c.id ).map { t =>

            val expiredInSecs = ( t.expiredAfter.getTime - t.issuedAt.getTime ) / 1000

            Ok(
              Json.obj("token_type" -> "bearer",
                "access_token" -> t.token, "expire_in" -> expiredInSecs, "scope" -> "none" )
            )
          }

        }
        case None => Future { NotFound( Json.obj("message" -> "Client not found.", "severity" -> "ERROR") ) }
      }

    }

  }


  private def issueAccessTokenByCorrectCredential( clientId: Int ): Future[AccessToken] = {

    /*
    tokenRepo.findExistTokenByClientId( clientId ).flatMap {
      case Some( token ) => Future.successful( token )
      case None          => createAccessToken( clientId )
    }
   */

    tokenRepo.expireExistTokenByClientId( clientId )
    createAccessToken( clientId )
    // If the clientId has existing access token, expire it then create a new one for the client

  }


  private def createAccessToken( clientId: Int, expiredIn: Int = 3600 ): Future[AccessToken] = {

    def generateToken = UUID.randomUUID().toString

    val issuedAt      = LocalDateTime.now()
    val expiredAfter  = issuedAt.plusSeconds( expiredIn )

    tokenRepo.create( generateToken, issuedAt, expiredAfter, YesNoBoolean.No, clientId )

  }


} //:~
