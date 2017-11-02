package models

import play.api.libs.functional.syntax._
import play.api.libs.json._

/*
{
  "success": {
    "code": 0,
    "data": {
      "token": "NX1_25883128_RVJvb3VI....{removed}...WtNbVNjPQ2",
      "user_no": 25883128,
      "prod_id": "10800",
      "access_ip": "10.8.25.144",
      "auth_type": "OAUTH",
      "expires_in": 7177
    }
  }
}
*/

case class TokenDetailResponse(
  success : TokenDetailResponseDetail
)

case class TokenDetailResponseDetail(
  code: Int = 0,
  data: TokenDetailResponseData
)

case class TokenDetailResponseData (
  token: String = "",
  user_no: Int = 0,
  prod_id: String = "",
  access_ip: String = "",
  auth_type: String = "",
  expires_in: Int = 0
)

object TokenDetailResponseData {
  implicit val tokenDetailResponseDataFormat = Json.format[TokenDetailResponseData]
}

object TokenDetailResponseDetail {
  implicit val tokenDetailResponseDetailFormat = Json.format[TokenDetailResponseDetail]
}

object TokenDetailResponse {
  implicit val tokenDetailResponseFormat = Json.format[TokenDetailResponse]
}

//object TokenDetailResponseSuccess {
//  /*
//    def GetToken(token:Option[TokenResponse]) = token match {
//      case None => ""
//      case Some(t) => t.token
//    }
//  */
//}

//object ErrorResponse {
//  val errorDetailRead : Reads[ErrorDetail] = (
//    (JsPath \ "code").read[String] and
//      (JsPath \ "type").read[String] and
//      (JsPath \ "message").read[String]
//    )(ErrorDetail.apply _)
//  val errorDetailWrites : Writes[ErrorDetail] = (
//    (JsPath \ "code").write[String] and
//      (JsPath \ "type").write[String] and
//      (JsPath \ "message").write[String]
//    )(unlift(ErrorDetail.unapply))
//  implicit val errorDetailFormat: Format[ErrorDetail] = Format(errorDetailRead, errorDetailWrites)
//  implicit val errorResponseFormat = Json.format[ErrorResponse]
//}
