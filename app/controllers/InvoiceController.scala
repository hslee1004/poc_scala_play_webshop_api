package controllers

import _root_.libs.{NxAPI, JupiterClient, JPCouchbase}
import akka.actor.ActorSystem
import javax.inject._
import models._
import play.api._
import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws.{WSResponse, WSRequest, WSClient}
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

@Singleton
class InvoiceController @Inject()(actorSystem: ActorSystem, ws: WSClient, config: play.api.Configuration)(implicit exec: ExecutionContext) extends Controller {
  // POST: Invoice, access_token
  def CreateInvoice() = Action.async(BodyParsers.parse.json) { request =>
    implicit val invoiceFormat = Json.format[Invoice]
    request.body.validate[Invoice].map {
      v => {
        println("init invoice:" + v.toString)
        val promise = Promise[Result]()
        val f:Future[APIResponse[Invoice]] = for {
          //token <- NxAPI.GetTokenExt(v.ticket.getOrElse(""), ws)
          ticket <- NxAPI.getResponse[TicketResponse](createTicketRequest(v.access_token))
          inv <- Future {Invoice.saveInvoice(v, TicketResponse.GetTicket(ticket._1))}
        } yield APIResponse[Invoice](Some(v), ticket._2, "")
        f.map { rs =>
          rs.response match {
            case Some(v) =>  promise.success(
              Ok(Json.toJson(InvoiceResponse(v.ticket.getOrElse(""), "http://billing.nexon.net/shop/cart?")))
            )
            case None =>  {
              rs.errorResponse match {
                case Some(e) => promise.success(Ok(Json.toJson(e)))
                case None => promise.success(Ok("none"))
              }
            }
          }
        }
        promise.future
      }
    } getOrElse {
      Future {BadRequest(Json.obj("code" ->"401", "message" -> "wrong parameter."))}
    }
  }

  // GET invoice
  def GetInvoice(ticket: String) = Action.async {
    println("[InvoiceController][TestInvoice]: ticket:" + ticket)
    implicit val invoiceFormat = Json.format[Invoice]
    // header : token
    val promise = Promise[Result]()
    val f:Future[APIResponse[Invoice]] = for {
      //token <- NxAPI.GetToken(createTokenRequest("dd"))
      token <- NxAPI.getResponse[TokenResponse](createTokenRequest("dd"))
      invoice <- JPCouchbase.Get(ticket)
      //result <- NxAPI.GetTokenExt("ddd", ws)
    } yield APIResponse[Invoice](Some(invoice.get), token._2, "")
    f.map { rs =>
      rs.response match {
        case Some(v) =>  promise.success(
          Ok(Json.toJson(rs.response))
        )
        case None =>  {
          rs.errorResponse match {
            case Some(e) => promise.success(Ok(Json.toJson(e)))
            case None => promise.success(Ok("none"))
          }
        }
      }
    }  recover {
      case e:Exception => {
        println("[InvoiceController][GetInvoice] error: " + e.getMessage)
        promise.success(Ok(Json.obj("code" ->"404", "message" -> "wrong invoice.")))
        (None)
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

  //
  // test
  //
  def TestCreateSampleInvoice() = Action.async {
    println("[InvoiceController][TestInvoice]: start.")
    implicit val invoiceFormat = Json.format[Invoice]
    //val promise = Promise[Result]()
    JPCouchbase.Save(Invoice.CreateDemoInvoice())
    //promise.success(Ok("ok"))
    Future { Ok("ok")}
  }

  // test invoice json
  def TestInvoiceJson(ticket: String) = Action.async {
    // test
    println("[InvoiceController][TestInvoiceJson]: ")
    //AppConfig.GetNXAPIUrl(config, "tikcet")
    //AppConfig.GetNXAPIUrl(config, "tikcet")

    Future {
      val invoice = Invoice("2000", ticket = Some("1234"))
      Ok(Json.toJson(invoice))
    }
  }

  /*
    def TestInvoiceXML(ticket: String) = Action {
      val invoice = Invoice("2000", ticket = Some("1234"))
      Ok(Json.toJson(invoice))
    }
  */

  // test future recover
  def TestFutureJson() = Action.async {
    val promise = Promise[Result]()
    val f:Future[Option[InvoiceResponse]] = Future {
      println("[InvoiceController][TestFutureJson]")
      val dt = """{"product_id":"2000","user_no":"dd","user_id":"mantistest3","user_ip":"208.85.1112.109","date":"1465261178","items":[{"id":"100","name":"item_1","price":1,"qty":1},{"id":"200","name":"item_2","price":1,"qty":1}],"total_price":2,"ticket":"1","access_token":"NX1_1000000974_UXRHeVc2SkNCSGoyTDdnbHBaekhWemxWQWNzUG10K2NDdHI2N3ZNd2dYMzRpMkluUkhwV0g2OHRsZXQrYU8zcC94Q0RvNXJIaFE4TVNJU2YwL3pXaDdPaDVYRmJjbkVuZjN2eHY1RnBXbmwyRGtlM0o0c08wYXozQlRqVUs1TWczVlQyaHczSGhpdm5UaGVoZEduTzRoVzVRMGFNblNDZFNCNzNYbDhNc2Q0PQ2","redirect_uri":"localhost?ticket"}"""
      Some(Json.parse(dt).as[InvoiceResponse])
      /*
      == or ==
      try {
        println("redirect_uri:   " + dt)
        val invoiceResponse = Json.parse(dt).as[InvoiceResponse]
        println("redirect_uri:" + invoiceResponse.ticket)
      } catch {
        case e:Exception => {println("error message:" + e.getMessage);Json.obj("error" -> e.getMessage)}
        //error message:JsResultException(errors:List((/billing_url,List(ValidationError(List(error.path.missing),WrappedArray())))))
      }
      */
    } recover {
      case e:Exception => {
        println("error: " + e.getMessage)
        promise.success(Ok("ok"))
        (None)
      }
    }
    f.map {
      rs =>
      Ok("ddd")
    }
    promise.future
  }

}

