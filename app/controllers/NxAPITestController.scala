package controllers

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import libs.NxAPI
import play.api.libs.ws.WSClient
import play.api.mvc.{Controller, Action}

import scala.concurrent.{Future, ExecutionContext}

@Singleton
class NxAPITestController @Inject() (actorSystem: ActorSystem, ws: WSClient)(implicit exec: ExecutionContext) extends Controller {
  // GetToken
  def GetToken(ticket:String) = Action.async {
    //depricated NxAPI.GetTokenEx(ticket, ws).map{ msg => Ok(msg).as("text/json")}
    Future { Ok("ok") }
  }
}
