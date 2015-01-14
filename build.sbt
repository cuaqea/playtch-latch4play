name := "playtch-latch4play"

organization := "com.cuaqea.playtch"

version := "1.0"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "com.wordnik"  % "swagger-play2_2.10" % "1.3.2",
  "org.webjars" % "webjars-play" % "2.0",
  "org.webjars" % "bootstrap" % "2.1.1",
  "org.mongodb" % "mongo-java-driver" % "2.11.3",
  "org.mongodb" %% "casbah" % "2.6.4",
  "joda-time" % "joda-time" % "2.3",
  "de.undercouch" % "bson4jackson" % "2.2.0",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.2.1" % "optional",
  "org.apache.poi" % "poi" % "3.10-beta2",
  "org.apache.poi" % "poi-ooxml" % "3.10-beta2",
  "com.typesafe" % "config" % "1.0.2",
  "com.microsoft.windowsazure" % "microsoft-windowsazure-api" % "0.4.6",
  "com.notnoop.apns" % "apns" % "0.2.3",
  "org.testng" % "testng" % "6.8.8",
  "com.google.code.gson" % "gson" % "2.3.1",
  "com.ning" % "async-http-client" % "1.7.19"
)     

play.Project.playJavaSettings
