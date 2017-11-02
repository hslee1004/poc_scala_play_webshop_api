package libs

import models.Invoice

import scala.concurrent.ExecutionContext.Implicits.global
import org.reactivecouchbase.ReactiveCouchbaseDriver
// import the implicit JsObject reader and writer
import org.reactivecouchbase.CouchbaseRWImplicits.documentAsJsObjectReader
import org.reactivecouchbase.CouchbaseRWImplicits.jsObjectToDocumentWriter
import scala.concurrent.Future
import play.api.libs.json._
import net.liftweb.json._
import net.liftweb.json.Serialization.write
import play.api.libs.json._

/**
  * http://reactivecouchbase.org/
  * https://github.com/ReactiveCouchbase/reactivecouchbase-starter-kit
  * https://github.com/ReactiveCouchbase/reactivecouchbase-starter-kit/blob/master/build.sbt
  *
  * https://github.com/knoldus/scala-couchbase/blob/master/src/test/scala/com/couchbase/CouchbaseConnectionTest.scala
  * https://github.com/knoldus/scala-couchbase/blob/master/src/main/scala/com/couchbase/CouchbaseConnection.scala
  *
  */
object JPCouchbase {
  // get a driver instance driver
  val driver = ReactiveCouchbaseDriver()
  val bucket = driver.bucket("jupiterapi_shop")  // jupiterapi_shop, default

  def Save(invoice:Invoice):String = {
    // create
    val js = Json.toJson(invoice)
    bucket.set[Invoice](invoice.ticket.getOrElse(""), invoice).onSuccess {
      case status => println(s"Operation status : ${status.getMessage}")
    }
    invoice.ticket.getOrElse("")
  }

  def Get(ticket:String): Future[Option[Invoice]] = {
    println("[JPCouchbase][Get]: ticket: " + ticket)
    if (ticket == "") {
      println("[JPCouchbase][Get]: ticket is empty")
      return Future {None}
    }
    bucket.get[Invoice](ticket)
  }
  // shutdown the driver (only at app shutdown)
  //driver.shutdown()
}