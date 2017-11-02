package models

import play.api.libs.json.Json

case class InvoiceResponse(
  ticket: String = "",
  billing_url:String = ""
)

object InvoiceResponse {
  implicit val ticketResponseFormat = Json.format[InvoiceResponse]
  def SafeParse(data:String):InvoiceResponse = {
      try {
        Json.parse(data).as[InvoiceResponse]
      } catch {
        case e:Exception => {println("[InvoiceResponse][SafeParse]:" + e.getMessage);InvoiceResponse()}
      }
  }

  def GetInvoice(invoice:Option[InvoiceResponse]) = invoice match {
    case None => ""
    case Some(t) => t.ticket
  }
}
