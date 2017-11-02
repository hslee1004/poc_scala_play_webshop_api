package models

import play.api.libs.json.Json

case class Balance(
  Total: Float,
  Prepaid: Float,
  Credit: Float
)

object Balance {
  implicit val balanceFormat = Json.format[Balance]
}
