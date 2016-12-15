enablePlugins(ScalaJSPlugin)

name := "nagel-schreckenberg"

scalaVersion := "2.12.1"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "org.typelevel" %% "cats" % "0.8.1"
libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.1"
libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.6.2"

lazy val root = project.enablePlugins(ScalaJSPlugin)