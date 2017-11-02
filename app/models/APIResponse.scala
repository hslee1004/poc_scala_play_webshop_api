package models

import play.api.libs.json.Json

/*
case class APIResponse(
  var invoice: Option[Invoice],
  var tokenResponse: Option[TokenResponse],
  var errorResponse: Option[ErrorResponse]
)
*/

case class APIResponse[T](
  var response: Option[T],
  var errorResponse: Option[ErrorResponse],
  var data: String
)

object APIResponse {
  //implicit val apiResponseFormat = Json.format[APIResponse[T]]
}
