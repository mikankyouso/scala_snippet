import AssemblyKeys._

seq(assemblySettings: _*)

seq(Twirl.settings: _*)

name := "scala_snippet"

version := "0.0"

scalaVersion := "2.9.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

javacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-source", "1.6",
  "-target", "1.6"
)

//autoCompilerPlugins := true

//addCompilerPlugin("org.scala-lang.plugins" % "continuations" % "2.9.1")

scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  //"-P:continuations:enable",
  "-deprecation"
)

libraryDependencies ++= Seq(
  "org.apache.lucene" % "lucene-core" % "3.5.0",
  "org.apache.lucene" % "lucene-analyzers" % "3.5.0",
  "org.apache.lucene" % "lucene-highlighter" % "3.5.0",
  "org.apache.lucene" % "lucene-queries" % "3.5.0",
  "org.apache.lucene" % "lucene-demo" % "3.5.0",
  "com.google.guava" % "guava" % "11.0.2",
  "org.scalaz" %% "scalaz-core" % "6.0.3",
  "com.typesafe.akka" % "akka-actor" % "2.0",
  "com.github.scala-incubator.io" %% "scala-io-core" % "0.3.0",
  "org.scala-tools.time" %% "time" % "0.5",
  //"com.github.jsuereth.scala-arm" %% "scala-arm" % "1.1",
  "org.scala-tools" %% "scala-stm" % "0.4",
  "org.squeryl" %% "squeryl" % "0.9.5-RC1",
  "org.scalatest" %% "scalatest" % "1.6.1" % "test",
  "org.scala-tools.testing" %% "scalacheck" % "1.9" % "test",
  "junit" % "junit" % "4.10" % "test"
)

//artifactName := { (config: String, module: ModuleID, artifact: Artifact) =>
//  artifact.name + "-" + module.revision + "." + artifact.extension
//}

EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE16)

EclipseKeys.withSource := true
