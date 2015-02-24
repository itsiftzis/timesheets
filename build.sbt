import play.PlayJava

name := """mmbu-timesheets"""

organization := "com.mgage.mmbu.internalapps.timesheets"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.5"

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "Apache Maven" at "http://mvnrepository.com/artifact"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "net.vz.mongodb.jackson" % "mongo-jackson-mapper" % "1.4.2",
  "com.google.inject" % "guice" % "3.0",
  "javax.inject" % "javax.inject" % "1",
  "com.google.code.gson" % "gson" % "2.2.4",
  "net.sf.flexjson" % "flexjson" % "3.2"
)
