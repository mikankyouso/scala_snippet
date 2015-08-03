name := "scala_snippet"

version := "0.0"

scalaVersion := "2.11.7"

//scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature", "-P:continuations:enable")

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature")

javacOptions ++= Seq("-encoding", "UTF-8", "-source", "1.8", "-target", "1.8")

libraryDependencies ++= Seq(
  //"org.apache.solr" % "solr-core" % "4.4.0" excludeAll(ExclusionRule(organization = "org.restlet.jee")),
  //"org.jruby" % "jruby" % "1.7.4",
  //"org.codehaus.groovy" % "groovy-jsr223" % "2.1.6",
  "com.itextpdf" % "itextpdf" % "5.4.3",
  "com.itextpdf" % "itext-asian" % "5.2.0",
  "org.scalaz" %% "scalaz-core" % "7.1.3",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3",
  "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3",
  "com.google.guava" % "guava" % "18.0",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.12",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  //"org.specs2" %% "specs2" % "3.2" % "test",
  //"org.pegdown" % "pegdown" % "1.4.1" % "test",
  "com.github.nscala-time" %% "nscala-time" % "1.8.0",
  "junit" % "junit" % "4.12" % "test",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)

EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE18)

EclipseKeys.withSource := true

(testOptions in Test) += Tests.Argument(TestFrameworks.Specs2, "junitxml", "console", "html")

net.virtualvoid.sbt.graph.Plugin.graphSettings