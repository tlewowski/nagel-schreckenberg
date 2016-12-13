//enablePlugins(ScalaJSPlugin)

name := "Scala.js Tutorial"

scalaVersion := "2.12.1"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "org.typelevel" %% "cats" % "0.8.1"

lazy val root = project.enablePlugins(ScalaJSPlugin)