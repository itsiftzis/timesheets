import play.PlayJava

name := """play-db"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "net.vz.mongodb.jackson" % "mongo-jackson-mapper" % "1.4.2"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "play2-crud" % "play2-crud_2.10" % "0.7.0"
)
