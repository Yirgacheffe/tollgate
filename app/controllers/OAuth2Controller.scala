package controllers

import javax.inject._
import play.api.mvc._



class OAuth2Controller @Inject() ( cc: ControllerComponents ) extends AbstractController( cc ) {

  def token() = Action { implicit request: Request[AnyContent] =>

    Ok( "this is from token endpoint" )

  }

} //:~
