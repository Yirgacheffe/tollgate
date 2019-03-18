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

  /*
  val grantType = ""
  val clientId  = ""
  val clientSecret = ""
  val scope = ""
  */


  def token() = Action { implicit request =>

    val isRight = for {
      params <- request.body.asFormUrlEncoded
      if params.nonEmpty
      grantType    <- params.get( "grant_type" )
      clientId     <- params.get( "client_id"  )
      clientSecret <- params.get( "client_secret" )
      if grantType.nonEmpty && clientId.nonEmpty && clientSecret.nonEmpty
    } yield {
      1
    }

    if ( isRight.nonEmpty ) {
      Ok( "it is ok" )
    } else {
      BadRequest( Json.obj( "error" -> "Bad request" ) )
    }



/*
    val params = request.body.asFormUrlEncoded


    if (params.isEmpty) {
      Ok(Json.toJson("{Error}"))
    } else {
      Ok(params.toString)
    }
*/

    /*
    val grantType = params.getOrElse( "grant_type", None )

    logger.info( "Get parameter grant_type: " + grantType )

    repo.list().map { client =>
      Ok( Json.toJson(client) )
    }

  }
*/


  }



} //:~
