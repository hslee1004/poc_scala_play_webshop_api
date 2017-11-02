package libs

import javax.inject.Singleton
import javax.security.auth.login.AppConfigurationEntry

import com.google.inject.Inject
import models._
import play.api.Configuration
import play.api.http.Writeable
import play.api.libs.json._
import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws.{WSResponse, WSClient}

import scala.concurrent.{ExecutionContext, Future}

object NxAPI {
  //
  def getResponse[T](response:Future[WSResponse])(implicit wr: Writes[T], rd: Reads[T]): Future[(Option[T], Option[ErrorResponse])] = {
    import ExecutionContext.Implicits.global
    response
      .map {
        rs => {
          println("response: " + rs.status + ", body: " + rs.body)
          rs.status match {
            case 200 => (Some(Json.parse(rs.body).as[T]), None)
            case _ => (None, Some(Json.parse(rs.body).as[ErrorResponse]))
            //case _ => throw new APIException(rs.body)
          }
        }
  }  recover {
    case e:Exception => {
      println("[getReponse] exception: " + e.getMessage)
      (None, None)
    }
  }

  }

  def GetTicket(response:Future[WSResponse]): Future[(Option[TicketResponse], Option[ErrorResponse])] = {
    import ExecutionContext.Implicits.global
    println("[NxAPI][GetTicket]")
    response
      .map {
        rs => {
          println("response GetTicket: " + rs.status + ", body: " + rs.body)
          rs.status match {
            case 200 => (Some(Json.parse(rs.body).as[TicketResponse]), None)
            case _ => (None, Some(Json.parse(rs.body).as[ErrorResponse]))
            //case _ => throw new APIException(rs.body)
          }
        }
      }
  }

  def GetTokenDetail(response:Future[WSResponse]):Future[(Option[TokenDetailResponse], Option[ErrorResponse])] ={
    import ExecutionContext.Implicits.global
    response
      .map {
        rs => {
          println("response token detail: " + rs.status + ", body: " + rs.body)
          rs.status match {
            case 200 => (Some(Json.parse(rs.body).as[TokenDetailResponse]), None)
            case _ => (None, Some(Json.parse(rs.body).as[ErrorResponse]))
            //case _ => (None, None)
          }
        }
    }
  }

  // func (s *NXAPIService) GetUserId(token string) (*models.UserIdResponse, bool) {

  def createTokenRequest(ticket:String, ws: WSClient, config:play.api.Configuration):Future[WSResponse] = {
    val data = Json.obj(
      "ticket" -> ticket,
      "product_id" -> "20000",
      "secret_key" -> "23a3fd9d2ecb9d0cb1e592f593e338c71f95ce54"
    )
    //createPostRequest("http://api.nexon.net/auth/ticket", data, ws)
    createPostRequest("http://api.nexon.net/auth/token", data, ws)
  }

  def createTicketRequest(token:String, ws: WSClient, config:play.api.Configuration):Future[WSResponse] = {
    val data = Json.obj(
      "token" -> token,
      "product_id" -> "20000"
    )
    createPostRequest("http://api.nexon.net/auth/ticket", data, ws)
  }

  def CreateTokenDetailRequest(token:String, ws:WSClient, config:play.api.Configuration):Future[WSResponse] = {
    val params = List("access_token" -> token)
    println("createTokenDetailRequest: " + token)
    createGetRequest("http://api.nexon.net/auth/token", params, ws)
  }

  def createPostRequest(url:String, data:JsObject, ws:WSClient):Future[WSResponse] = {
    ws.url(url)
      .withHeaders("Accept" -> "application/json")
      .post(data)
  }

  def createGetRequest(url:String, params:List[(String, String)], ws:WSClient):Future[WSResponse] = {
    ws.url(url)
      .withHeaders("Accept" -> "application/json")
      .withQueryString(params : _*)
      .get()
  }

  //
  // old code - will be depricated
  //

  //
  def GetTokenExt(ticket:String, ws: WSClient): Future[(Option[TokenResponse], Option[ErrorResponse])] = {
    import ExecutionContext.Implicits.global
    // verify tikcet
    if (ticket == "") {
      Future {
        (None, Some(ErrorResponse(ErrorDetail("400", "parameter error - ticket"))))
      }
    } else {
      val data = Json.obj(
        "ticket" -> ticket,
        "product_id" -> "20000",
        "secret_key" -> "23a3fd9d2ecb9d0cb1e592f593e338c71f95ce54"
      )
      ws.url("http://api.nexon.net/auth/token")
        .withHeaders("Accept" -> "application/json")
        .post(data)
        .map {
          rs => {
            println("api.nexon.net/auth/token: " + rs.status + ", body: " + rs.body)
            rs.status match {
              case 200 => (Some(Json.parse(rs.body).as[TokenResponse]), None)
              case _ => (None, Some(Json.parse(rs.body).as[ErrorResponse]))
              //case _ => throw new APIException(rs.body)
            }
          }
        }
    }
  }

  //
  // response
  //   {"ticket":"3765f4ea-5c4c-475a-9009-c23928b42b8c"}
  //	 {"error":{"code":"6","type":"Bad Request","message":"Parameter is missing.(product id)"}}
  //
  def GetTicket(token:String, ws: WSClient): Future[(Option[TicketResponse], Option[ErrorResponse])] = {
    import ExecutionContext.Implicits.global
    println("[NxAPI][GetTicket]: token:" + token)
    if (token == "") {
      Future {
        (None, Some(ErrorResponse(ErrorDetail("400", "parameter error - ticket"))))
      }
    } else {
      val data = Json.obj(
        "token" -> token,
        "product_id" -> "20000"
      )
      ws.url("http://api.nexon.net/auth/ticket")
        .withHeaders("Accept" -> "application/json")
        .post(data)
        .map {
          rs => {
            println("api.nexon.net/auth/ticket: " + rs.status + ", body: " + rs.body)
            rs.status match {
              case 200 => (Some(Json.parse(rs.body).as[TicketResponse]), None)
              case _ => (None, Some(Json.parse(rs.body).as[ErrorResponse]))
              //case _ => throw new APIException(rs.body)
            }
          }
        }
    }
  }

  //
  // test
  //

  // test json
  def TestJson(): String = {
    val js = Json.toJson(Invoice("2001"))
    js.toString()
  }

  /*
  def GetToken(response:Future[WSResponse]): Future[(Option[TokenResponse], Option[ErrorResponse])] = {
    import ExecutionContext.Implicits.global
    response
      .map {
        rs => {
          println("response GetToken: " + rs.status + ", body: " + rs.body)
          rs.status match {
            case 200 => (Some(Json.parse(rs.body).as[TokenResponse]), None)
            case _ => (None, Some(Json.parse(rs.body).as[ErrorResponse]))
            //case _ => throw new APIException(rs.body)
          }
        }
      }
  }
  */

  /*
    def GetTokenDetail(token:String, ws:WSClient):Future[String] ={
      import ExecutionContext.Implicits.global
      val params = List("access_token" -> token)
      var fReq = createGetRequest("http://api.nexon.net/auth/ticket", params, ws)
      //   def CreateTokenDetailRequest(token:String, ws:WSClient, config:play.api.Configuration):Future[WSResponse] = {

      fReq.map  {
        rs => {
          println("api.nexon.net/auth/ticket: " + rs.status + ", body: " + rs.body)
          rs.status match {
            //case 200 => (Some(Json.parse(rs.body).as[TicketResponse]), None)
            //case _ => (None, Some(Json.parse(rs.body).as[ErrorResponse]))
            case _ => "test_result"
          }
        }
      }
    }
  */
}
