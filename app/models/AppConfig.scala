package models

import javax.inject._
import play.api.Configuration
import play.api.libs.json.Json
import play.api.inject._

import play.api.Play.current

import scala.concurrent.ExecutionContext

case class NameValue(name:String, value:String)

//
// http://stackoverflow.com/questions/17196166/hocon-read-an-array-of-objects-from-a-configuration-file
//
trait AppConfig {
  def configuration: Configuration
  def GetNXAPIUrl (api:String):String = {
    println("[ConfigTrait][GetAPIUrl]: " + api)
    val str = configuration.getString("nxapi.api." + api).getOrElse("")
    println("GetAPIUrl: " + str)
    str
  }
  /*
    def nxapis = for {
      projectsFound <- configuration.getConfigList("projects").toList
      projectConfig <- projectsFound
      name <- projectConfig.getString("name").toList
      value  <- projectConfig.getString("url").toList
    } yield NameValue(name, value)
  */
}

@Singleton
class JupiterAppConfig @Inject() (val configuration: play.api.Configuration) extends AppConfig

import javax.inject._
import play.api.Configuration

//trait AppConfigTrait {
//  def configuration: Configuration
//  def projects = for {
//    projectsFound <- configuration.getConfigList("projects").toList
//    projectConfig <- projectsFound
//    name <- projectConfig.getString("name").toList
//    url  <- projectConfig.getString("url").toList
//  } yield Project(name,url)
//}
//
//case class Project(name: String, url: String)
//class AppConfig2 @Inject() (val configuration: Configuration) extends AppConfigTrait


//case class AppConfiguration @Inject() (val appConfig:AppConfig)() {
//  def GetNXAPIUrl (api:String):String = {
//    println("[ConfigTrait][GetAPIUrl]: " + api)
//    val str = appConfig.configuration.getString("nxapi.api." + api).getOrElse("")
//    println("GetAPIUrl: " + str)
//    str
//  }
//}

//case class AppConfiguration @Inject() (val appConfig:AppConfig)(name:String) {
//  def GetTest() = {
//      appConfig.configuration.getString("test")
//  }
//}

//case class AppConfiguration(val configuration: play.api.Configuration) extends AppConfig

//  /*
//    var appConfig:AppConfig = _
//
//    def a(): AppConfig = {
//      import ExecutionContext.Implicits.global
//      println("[AppConfig][apply]: called")
//      appConfig = AppConfig()
//      appConfig
//    }
//  */
//}
