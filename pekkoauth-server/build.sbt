name := """PekkoAuth-server"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "3.3.1"

libraryDependencies ++= Seq(
guice,
ws,
"org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test,
"org.playframework" %% "play-slick" % "6.0.0-M2",
"com.h2database" % "h2" % "1.4.200",
"ch.qos.logback" % "logback-classic" % "0.9.28" % "test",
"org.postgresql" % "postgresql" % "42.5.1",
filters,
)
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
