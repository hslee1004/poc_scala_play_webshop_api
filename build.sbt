name := """play-scala-jupiter"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

resolvers += "ReactiveCouchbase Releases" at "https://raw.github.com/ReactiveCouchbase/repository/master/releases/"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "org.reactivecouchbase" %% "reactivecouchbase-core" % "0.3",
  "net.liftweb" % "lift-json_2.11" % "2.6.2",
  "net.liftweb" % "lift-json-ext_2.11" % "2.6.2"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
