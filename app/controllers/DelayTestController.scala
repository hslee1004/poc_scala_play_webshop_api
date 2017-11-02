package controllers

import javax.inject._
import _root_.libs._
import akka.actor.ActorSystem
import models.State
import play.api._
import play.api.mvc._

import scala.concurrent.{Future, Promise, ExecutionContext}

@Singleton
//class DelayTestController @Inject() extends Controller {
class DelayTestController @Inject() (actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends Controller {

  // test
  def index(delay:Int) = Action {
    println("[DelayTestController].[index]: \n")
    Thread.sleep(delay * 1000)
    Ok("response - ok...")
    //Ok(views.html.index("Your new application is ready."))
  }

  def getJson() = Action {
    Ok(NxAPI.TestJson())
  }

  //
  // http://stackoverflow.com/questions/25897810/accessing-a-custom-dispatcher-defined-in-application-conf-inside-a-scala-trait
  // http://doc.akka.io/docs/akka/snapshot/scala/dispatchers.html
  //

  //val system = akka.actor.ActorSystem("my-context")    // this was missing!
  //implicit val myExecutionContext: ExecutionContext = system.dispatchers.lookup("my-context")
  //implicit val ec = context.system.dispatchers.lookup("akka.my-batch-dispatcher")

  def TestFuture = Action.async {
    //import ExecutionContext.Implicits.global
    //xAPI.TestFuture().map{ msg => Ok(msg)}
    Future {Ok("plz remove...")}
  }

  def TestPromise = Action {
    val promise = Promise[String]()
    promise.future
    Ok("test")
  }


}

