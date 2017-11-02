package controllers

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import libs.{NxCashbroker, NxAPI}
import models.{Item, Invoice}
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc.{Controller, Action}

import scala.concurrent.ExecutionContext

@Singleton
class NxCashbrokerController @Inject() (actorSystem: ActorSystem, ws: WSClient)(implicit exec: ExecutionContext) extends Controller {
  // get balance
  def GetBalance(userId:String) = Action.async {
    println("userId: " + userId)
    NxCashbroker.GetBalance(userId, ws).map{ balance => Ok(Json.toJson(balance))}
  }

  def TestXML() = Action {
    var xml = Item("id_1", "name_1", 1, 1).toXml
    Ok(xml)
  }

  // request payment
  def TestRequestPayment = Action.async {
    var items = List(Item("123", "name_1", 1, 1), Item("12345", "name_2", 1, 1))
    NxCashbroker.RequestPayment(items, ws).map { rs => Ok(Json.toJson(rs._2))}
  }
}
