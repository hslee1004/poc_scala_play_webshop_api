package models

import play.api.libs.json.Json

/*
{
  "ticket": "3765f4ea-5c4c-475a-9009-c23928b42b8c"
}
{
  "error": {
    "code": "6",
    "type": "Bad Request",
    "message": "Parameter is missing.(product id)"
  }
}
*/
case class TicketResponse(
  ticket: String = ""
)

object TicketResponse {
  implicit val ticketResponseFormat = Json.format[TicketResponse]
  def GetTicket(ticket:Option[TicketResponse]) = ticket match {
    case None => ""
    case Some(t) => t.ticket
  }
}
