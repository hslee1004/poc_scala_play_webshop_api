package models

import play.api.libs.json.Json

/*
{
  "token": "NX1_2588312M0pMYWJqUURnPQ2",
  "refresh_token": "NX1_258831NWxzPQ2",
  "expires_in": 21591,
  "auth_token": null
}
{
  "error": {
    "code": "6",
    "type": "Bad Request",
    "message": "Parameter is missing.(ticket)"
  }
}
*/
case class TokenResponse(
  token: String = "",
  refresh_token: String = "",
  expires_in: Int = 0,
  auth_token: String = ""
)

object TokenResponse {
  implicit val tokenRefreshFormat = Json.format[TokenResponse]
  def GetToken(token:Option[TokenResponse]) = token match {
    case None => ""
    case Some(t) => t.token
  }
}
//
//case class APIResponse(
//  var invoice: Option[Invoice],
//  var tokenResponse: Option[TokenResponse],
//  var errorResponse: Option[ErrorResponse]
//)
//
//object APIResponse {
//  implicit val apiResponseFormat = Json.format[APIResponse]
//}
