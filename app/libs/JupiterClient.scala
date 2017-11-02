package libs

import models.Invoice
import play.api.libs.json.Json
import play.api.libs.ws.WSClient


//trait InvoiceHandler[+A] { self =>
//  def a: A
//  def getTocken(ticket:String): self.type = {
//    this
//  }
//}

class JupiterClient(var underlying: Invoice)

trait JSClient[+A] { self =>
  var underlying: Invoice
  def name() = {
    underlying.product_id
  }
  def getToken(ws:WSClient, ticket:String): self.type = {
    underlying.product_id = ticket
    println("before NxAPI.GetToken...")
    //val rs = NxAPI.GetToken(ticket, ws)
    //println("getToken: response:" + rs)
    this
  }
}

object JupiterClient {
  //implicit val itemFormat = Json.format[Item]
  def apply(invoice: Invoice) = new JupiterClient(invoice) with JSClient[JupiterClient]
}

/*
class DBCollection(override val underlying: MongoDBCollection) extends ReadOnly

trait ReadOnly {
  val underlying: MongoDBCollection
  def name = underlying getName
  def fullName = underlying getFullName

  def find(query: Query): DBCursor = {
    def applyOptions(cursor:DBCursor, option: QueryOption): DBCursor = {
      option match {
        case Skip(skip, next)    => applyOptions(cursor.skip(skip), next)
        case Sort(sorting, next) => applyOptions(cursor.sort(sorting), next)
        case Limit(limit, next)  => applyOptions(cursor.limit(limit), next)
        case NoOption => cursor
      }
    }
    applyOptions(find(query.q), query.option)
  }

  def find(doc: DBObject): DBCursor = underlying find doc
  def findOne(doc: DBObject) = underlying findOne doc
  def findOne = underlying findOne
  def getCount(doc: DBObject) = underlying getCount doc
}
 */


//trait JSAPI {
//  def client: JupiterClient
//  def url(url: String): WSRequest
//}

//class JupiterClient {
//  // invoice: Invoice
//  def underlying[T]: T
//  def getToken(ticket: String): JSRequest
//}

trait TocketHandler {
  val underlying: Invoice
  def getTocken(ticket:String):Invoice = {
    Invoice("2000")
  }
}
//
//trait ReadOnly {
//  val underlying: MongoDBCollection
//  def name = underlying getName
//  def fullName = underlying getFullName
//
//  def find(query: Query): DBCursor = {
//    def applyOptions(cursor:DBCursor, option: QueryOption): DBCursor = {
//      option match {
//        case Skip(skip, next)    => applyOptions(cursor.skip(skip), next)
//        case Sort(sorting, next) => applyOptions(cursor.sort(sorting), next)
//        case Limit(limit, next)  => applyOptions(cursor.limit(limit), next)
//        case NoOption => cursor
//      }
//    }
//    applyOptions(find(query.q), query.option)
//  }
//
//  def find(doc: DBObject): DBCursor = underlying find doc
//  def findOne(doc: DBObject) = underlying findOne doc
//  def findOne = underlying findOne
//  def getCount(doc: DBObject) = underlying getCount doc
//}
