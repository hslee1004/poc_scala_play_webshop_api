package libs

//import akka.actor.FSM.Failure
import models._
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object NxJupiterAPI {
  // to find out error message
  def TestJson(): Unit = {
    val dt = """{"product_id":"2000","user_no":"dd","user_id":"mantistest3","user_ip":"208.85.1112.109","date":"1465261178","items":[{"id":"100","name":"item_1","price":1,"qty":1},{"id":"200","name":"item_2","price":1,"qty":1}],"total_price":2,"ticket":"1","access_token":"NX1_1000000974_UXRHeVc2SkNCSGoyTDdnbHBaekhWemxWQWNzUG10K2NDdHI2N3ZNd2dYMzRpMkluUkhwV0g2OHRsZXQrYU8zcC94Q0RvNXJIaFE4TVNJU2YwL3pXaDdPaDVYRmJjbkVuZjN2eHY1RnBXbmwyRGtlM0o0c08wYXozQlRqVUs1TWczVlQyaHczSGhpdm5UaGVoZEduTzRoVzVRMGFNblNDZFNCNzNYbDhNc2Q0PQ2","redirect_uri":"localhost?ticket"}"""
    try {
      println("redirect_uri:   " + dt)
      val invoiceResponse = Json.parse(dt).as[InvoiceResponse]
      println("redirect_uri:" + invoiceResponse.ticket)
    } catch {
      case e:Exception => {println("error message:" + e.getMessage);Json.obj("error" -> e.getMessage)}
    }
    // error message:JsResultException(errors:List((/billing_url,List(ValidationError(List(error.path.missing),WrappedArray())))))
    println("redirect_uri:   end")
  }

  // call jupiter invoice api
  def CreateInvoice(token:Option[TokenResponse], invoice:Invoice, ws: WSClient): Future[(Option[InvoiceResponse], Option[ErrorResponse], String)] = {
    import ExecutionContext.Implicits.global
    if (token == None) {
      Future { (None, None, "") }
    } else {
      // set token info to invoice
      invoice.access_token = TokenResponse.GetToken(token)
      invoice.user_no = "dd"
      println("[NxJupiterAPI][CallInvoiceAPI]: invoice:" + invoice.product_id)
      val data = Json.toJson(invoice).toString()
      println("invoice: " + data)
      ws.url("http://localhost:9000/api/invoice")
        .withHeaders("Accept" -> "application/json")
        //.post(data)
        .post(Json.toJson(invoice))
        .map {
          rs => {
            println("response jupiter invoice api : " + rs.status + ", body: " + rs.body)
            rs.status match {
              case 200 =>
                (Some(InvoiceResponse.SafeParse(rs.body)), None, rs.body.toString)
                //(Some(InvoiceResponse.Parse(rs.body)), None, rs.body.toString)
              case _ => (None, Some(Json.parse(rs.body).as[ErrorResponse]), rs.body.toString)
            }
          }
        }
      /*
      client.onComplete {
      case Success(rs)  =>
        rs.status match {
          case 200 => (Some(InvoiceResponse.Parse(rs.body)), None, rs.body.toString)
            //(Some(InvoiceResponse("")), None, rs.body.toString)
          case _ => (None, Some(Json.parse(rs.body).as[ErrorResponse]), rs.body.toString)
        }
      case Failure(e) => (None, None, "")
      }
      */
    }
  }

}
