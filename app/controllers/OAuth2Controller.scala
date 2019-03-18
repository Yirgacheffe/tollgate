package controllers

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.mvc._
import play.api.libs.json._

import models._


class OAuth2Controller @Inject() ( cc: ControllerComponents, repo: ClientRepository )( implicit ec: ExecutionContext )
    extends AbstractController( cc ) {


  def token() = Action.async { implicit request =>

    repo.list().map { client =>
      Ok( Json.toJson(client) )
    }

  }


} //:~
