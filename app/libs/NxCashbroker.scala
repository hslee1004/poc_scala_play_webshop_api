package libs

import models._
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}
import scala.xml.Elem

/**
  * https://www.playframework.com/documentation/2.5.x/ScalaWS
  * ws.url(url).withHeaders("Content-Type" -> "application/xml").post(xmlString)
  */

/*
response - error
<xmlCashBrokerSection type="Nexon.CashBroker.Universal.Entity.CheckBalanceResponse, Nexon.CashBroker.Universal, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null">
<CheckBalanceResponse xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<Result>50003</Result>
<DetailMessage/>
<Balance>0</Balance>
<BalanceByRule>0</BalanceByRule>
</CheckBalanceResponse>
</xmlCashBrokerSection>

response - balance
<xmlCashBrokerSection type="Nexon.CashBroker.Universal.Entity.CheckBalanceResponse, Nexon.CashBroker.Universal, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null">
<CheckBalanceResponse xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<Result>50000</Result>
<DetailMessage/>
<Balance>15224</Balance>
<BalanceByRule>15224</BalanceByRule>
</CheckBalanceResponse>
</xmlCashBrokerSection>

response - request payment
<xmlCashBrokerSection type="Nexon.CashBroker.Universal.Entity.RequestPaymentResponse, Nexon.CashBroker.Universal, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null">
  <RequestPaymentResponse xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <Result>50000</Result>
  <DetailMessage/>
  <TransactionId>F4EA9616CD464225BF0B96962428B0A1</TransactionId>
  </RequestPaymentResponse>
</xmlCashBrokerSection>

/*
<?xml version="1.0" encoding="utf-8" ?>
  <xmlCashBrokerSection type=" Nexon.CashBroker.Universal.Entity.RequestPaymentResponse, Nexon.CashBroker.Universal, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null">
    <RequestPaymentResponse>
      <Result>50000</Result>
      <TransactionId>43952FD2FEC5450AA9AE0124C9D538BE</TransactionId>
    </RequestPaymentResponse>
  </xmlCashBrokerSection>
*/

 */
object NxCashbroker {
  //
  def GetBalance(userId: String, ws: WSClient): Future[Balance] = {
    import ExecutionContext.Implicits.global
    // <?xml version="1.0" encoding="utf-8" ?>
    // ruleid_nx_prepaid = WSR2
    // don't use indent tool, no sapce between userId
    val data =
      <xmlCashBrokerSection type="Nexon.CashBroker.Universal.Entity.CheckBalanceRequest, Nexon.CashBroker.Universal, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null">
        <CheckBalanceRequest MethodName="CheckBalance">
          <Code>30201</Code>
          <UserId>{userId}</UserId>
          <RuleId>WSR2</RuleId>
        </CheckBalanceRequest>
      </xmlCashBrokerSection>
    ws.url("http://cashbroker16.nexon.net/CashBroker.aspx")
      .withHeaders("Content-Type" -> "application/xml")
      .post(data)
      .map {
        rs => {
          println("cashbroker16.nexon.net: " + rs.status + ", body: " + rs.body)
          rs.status match {
            case 200 => {
              val xml = scala.xml.XML.loadString(rs.body.toString)
              (xml \ "CheckBalanceResponse" \ "Result").text match {
                case "50000" => {
                  val balance = (xml \ "CheckBalanceResponse" \ "Balance").text
                  //nx_prepaid = WSR2
                  val balanceByRule = (xml \ "CheckBalanceResponse" \ "BalanceByRule").text
                  println("balance: " + balance + ", BalanceByRule: " + balanceByRule)
                  val nx = balance.toFloat - balanceByRule.toFloat
                  Balance(balance.toFloat, balanceByRule.toFloat, nx)
                }
                case _ => Balance(0, 0, 0)
              }
            }
            case _ => {
              s"${rs.body}"
              Balance(0, 0, 0)
            }
          }
        }
      }
  }

  def RequestPayment(items: List[Item], ws: WSClient): Future[(String, ResponseResult)] = {
    import ExecutionContext.Implicits.global
    var xmlItem = for (item <- items) yield {item.toXml}
    var userId = "mantistest3"
    // don't use indent tool, no sapce between userId
    val data = <xmlCashBrokerSection type="Nexon.CashBroker.Universal.Entity.RequestPaymentRequest, Nexon.CashBroker.Universal, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null">
      <RequestPaymentRequest MethodName="RequestPayment">
        <Code>30202</Code>
        <UserId>{userId}</UserId>
        <OrderID>TEST</OrderID>
        <RuleId>WSR1</RuleId>
        <ServiceCode>SVG035</ServiceCode>
        <CharacterId>0</CharacterId>
        <RemoteIp>192.168.1.234</RemoteIp>
        <RequestDate>2016-05-31</RequestDate>
        <ItemList>
          {xmlItem}
        </ItemList>
      </RequestPaymentRequest>
    </xmlCashBrokerSection>
    println("xml: " + data.toString())
    Future {
      "test - done... : request payment.."
    }
    ws.url("http://cashbroker16.nexon.net/CashBroker.aspx")
      .withHeaders("Content-Type" -> "application/xml")
      .post(data)
      .map {
        rs => {
          println("cashbroker16.nexon.net: " + rs.status + ", body: " + rs.body)
          rs.status match {
            case 200 => {
              val xml = scala.xml.XML.loadString(rs.body.toString)
              val result = (xml \ "RequestPaymentResponse" \ "Result").text
              result match {
                case "50000" => {
                  val trxId = (xml \ "TransactionId" \ "TransactionId").text
                  ("", ResponseResult("0", ""))
                }
                case _ => ("", ResponseResult("500", ""))
              }
            }
            case _ => {
              s"${rs.body}"
              (rs.status.toString, ResponseResult(rs.status.toString, "internal server error."))
            }
          }
        }
      }
    }

}
