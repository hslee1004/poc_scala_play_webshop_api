package controllers

import _root_.libs.{NxJupiterAPI, NxAPI, JupiterClient, JPCouchbase}
import akka.actor.ActorSystem
import javax.inject._
import models._
import play.api._
import play.api.libs.json.Json
import play.api.libs.ws.{WSResponse, WSRequest, WSClient}
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

@Singleton
class TestGameServerController @Inject()(actorSystem: ActorSystem, ws: WSClient, config: play.api.Configuration)(implicit exec: ExecutionContext) extends Controller {
  // simulate game server
  def TestRegisterInvoice(ticket: String) = Action.async {
    println("[TestGameServerController][TestRegisterInvoice]: ticket:" + ticket)
    val promise = Promise[Result]()
    val invoice = Invoice.CreateDemoInvoice()

    val f:Future[APIResponse[InvoiceResponse]] = for {
      token <- NxAPI.getResponse[TokenResponse](createTokenRequest(ticket))
      user <- NxAPI.getResponse[TokenDetailResponse](createTokenDetailRequest(TokenResponse.GetToken(token._1)))
      rs <- NxJupiterAPI.CreateInvoice(token._1, invoice, ws)
    } yield
      token._2 match {
        case Some(e) => APIResponse[InvoiceResponse] (rs._1, token._2, rs._3)
        case _ => APIResponse[InvoiceResponse] (rs._1, rs._2, rs._3)
    }
    f.map { rs =>
      rs.response match {
        case Some(v) =>  promise.success(Ok(Json.toJson(v)))
        case None =>  {
          rs.errorResponse match {
            case Some(e) => promise.success(Ok(Json.toJson(e)))
            case None => promise.success(Ok(Json.toJson(rs.errorResponse)))
          }
        }
      }
    }
    promise.future
  }

  // helper
  def createTokenRequest(ticket:String):Future[WSResponse] = {
    NxAPI.createTokenRequest(ticket, ws, config)
  }

  def createTicketRequest(token:String):Future[WSResponse] = {
    NxAPI.createTicketRequest(token, ws, config)
  }

  def createTokenDetailRequest(token:String):Future[WSResponse] = {
    NxAPI.CreateTokenDetailRequest(token, ws, config)
  }

}