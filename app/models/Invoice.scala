package models

import libs.JPCouchbase
import play.api.libs.json.Json
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.concurrent.Future

case class Invoice(
                    var product_id: String,
                    var user_no: String = "",
                    var user_id: String = "",
                    var user_ip: String = "",
                    var date: String = "",
                    var items: List[Item] = Nil,
                    var total_price: Float = 0,
                    var ticket: Option[String] = None,
                    var access_token: String = "",
                    var rule_id: Option[String] = None,
                    var redirect_uri: Option[String] = None,
                    var option_cash_type: Option[String] = None,  // empty is all, credit_only, prepaid_only
                    var option_use_receipt_flow: Option[String] = None,
                    var transaction_id: Option[String] = None    // of cashbroker
                  )

object Invoice {
  // https://www.playframework.com/documentation/2.5.x/ScalaJsonCombinators
  implicit val invoiceFormat = Json.format[Invoice]
  implicit val invoiceWrites = Json.writes[Invoice]
  implicit val invoiceReads = Json.reads[Invoice]

  def CreateDemoInvoice():Invoice = {
    Invoice("2000",
      user_no = "1111",
      user_id = "mantistest3",
      user_ip = "208.85.1112.109",
      date = "1465261178",
      items = List(Item("100", "item_1", 1, 1), Item("200", "item_2", 1, 1)),
      total_price = 2,
      ticket = Some("1"),
      access_token = "1111",
      //redirect_uri = Some("localhost?ticket")
      redirect_uri = Some("http://localhost/game/server/redirect")
    )
  }

  def verifyInvoice(invoice:Invoice):Boolean = {
    if (invoice.product_id == "") false
    if (invoice.user_id == "") false
    if (invoice.user_ip == "") false
    true
  }

  def saveInvoice(invoice: Invoice, ticket:String): String = {
    invoice.ticket = Some(ticket)
    JPCouchbase.Save(invoice)
  }

  def updateUserInfo(invoice:Invoice, token: TokenDetailResponse) = {
    //
    //invoice.user_id = token.success.data.user_no
  }
}
