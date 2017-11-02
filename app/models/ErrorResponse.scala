package models

import org.h2.engine.User
import play.api.libs.json._
import play.api.libs.functional.syntax._
/*
{
  "error": {
    "code": "6",
    "type": "Bad Request",
    "message": "Parameter is missing.(ticket)"
  }
}
*/
case class ErrorResponse(
  error : ErrorDetail
)

case class ErrorDetail(
  code: String = "",
  errorType: String = "",
  message: String = ""
)

// original
object ErrorResponse {
  //implicit val errorDetailFormat = Json.format[ErrorDetail]
  val errorDetailRead : Reads[ErrorDetail] = (
    (JsPath \ "code").read[String] and
    (JsPath \ "type").read[String] and
    (JsPath \ "message").read[String]
  )(ErrorDetail.apply _)
  val errorDetailWrites : Writes[ErrorDetail] = (
    (JsPath \ "code").write[String] and
    (JsPath \ "type").write[String] and
    (JsPath \ "message").write[String]
  )(unlift(ErrorDetail.unapply))
  implicit val errorDetailFormat: Format[ErrorDetail] = Format(errorDetailRead, errorDetailWrites)
  implicit val errorResponseFormat = Json.format[ErrorResponse]
}

//
// https://www.playframework.com/documentation/2.2.x/api/scala/index.html#play.api.libs.json.package
//
