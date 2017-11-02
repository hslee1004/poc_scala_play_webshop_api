package models

import play.api.libs.json.Json

case class ResponseResult (
  Code: String,
  Message: String
)
object ResponseResult {
  implicit val responseResultFormat = Json.format[ResponseResult]
}


//
//resp.Error = &models.NXAPIErrorDetail{
//Code:    "500",
//Message: err.Error(),
//}
//resp.Success = nil
