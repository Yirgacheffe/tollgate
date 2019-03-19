package controllers

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }

import play.api.mvc._
import play.api.Logger
import play.api.libs.json._

import models._


/**
  * OAuth2Controller handle all restful apis for oauth grant flow
  *
  * @version 1.0 $ 2019-03-18 17:19 $
  */
class OAuth2Controller @Inject() ( cc: ControllerComponents, repo: ClientRepository )( implicit ec: ExecutionContext )
    extends AbstractController( cc ) {


  private val logger: Logger = Logger( this.getClass )


  /**
    *
    */
  def token() = Action { implicit request =>


    val body   = request.body
    val params = body.asFormUrlEncoded.getOrElse( Map.empty[String, Seq[String]] )

    def param( name: String ): Option[String] = params.get( name ).flatMap( values => values.headOption )
    def requiredParam( name: String ): String = param( name ).getOrElse( "" )

    val grantType    = requiredParam( "grant_type"    )
    val clientId     = requiredParam( "client_id"     )
    val clientSecret = requiredParam( "client_secret" )

    if ( grantType.isEmpty || !grantType.equals("client_credentials")
                           || clientId.isEmpty
                           || clientSecret.isEmpty ) {
      BadRequest(
        Json.obj( "error_msg" -> "Invalid request body." ) )
    }


    val maybeCredential = ClientCredential( clientId, clientSecret )
    val clientAccount = repo.findClientCredential( maybeCredential )


    Ok( "dsfsd" + grantType.isEmpty + clientId.isEmpty + clientSecret.isEmpty )

    /*
    clientAccount.map {
      case Some(client) => Ok( "Ok for you!" )
      case None         => NotFound( "Client account not found!" )
    }*/

  }


} //:~
