import AssemblyKeys._

seq(assemblySettings: _*)

name := "scala_snippet"

version := "0.0"

scalaVersion := "2.9.1"

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")

javacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-source", "1.6",
  "-target", "1.6"
)

autoCompilerPlugins := true

addCompilerPlugin("org.scala-lang.plugins" % "continuations" % "2.9.1")

scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  //"-P:continuations:enable",
  "-deprecation"
)

libraryDependencies ++= Seq(
  "com.google.guava" % "guava" % "11.0.1",
  "org.scalaz" %% "scalaz-core" % "6.0.3",
  "se.scalablesolutions.akka" % "akka-actor" % "1.2",
  "com.github.scala-incubator.io" %% "scala-io-core" % "0.3.0",
  "org.scala-tools.time" %% "time" % "0.5",
  //"com.github.jsuereth.scala-arm" %% "scala-arm" % "1.1",
  "org.scala-tools" %% "scala-stm" % "0.4",
  "org.scalaquery" % "scalaquery_2.9.0-1" % "0.9.5",
  "org.scalatest" %% "scalatest" % "1.6.1" % "test",
  "org.scala-tools.testing" %% "scalacheck" % "1.9" % "test",
  "junit" % "junit" % "4.10" % "test"
)

//artifactName := { (config: String, module: ModuleID, artifact: Artifact) =>
//  artifact.name + "-" + module.revision + "." + artifact.extension
//}
