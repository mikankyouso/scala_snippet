name := "scala_snippet"

version := "0.0"

scalaVersion := "2.10.2"

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature", "-P:continuations:enable")

javacOptions ++= Seq("-encoding", "UTF-8", "-source", "1.7", "-target", "1.7")

libraryDependencies ++= Seq(
  //"org.apache.solr" % "solr-core" % "4.4.0" excludeAll(ExclusionRule(organization = "org.restlet.jee")),
  "org.jruby" % "jruby" % "1.7.4",
  "org.codehaus.groovy" % "groovy-jsr223" % "2.1.6",
  "com.itextpdf" % "itextpdf" % "5.4.3",
  "com.itextpdf" % "itext-asian" % "5.2.0",
  "org.scalaz" %% "scalaz-core" % "7.0.3",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.2",
  "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.2",
  "com.google.guava" % "guava" % "14.0.1",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.5",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "org.specs2" %% "specs2" % "2.1.1" % "test",
  "org.pegdown" % "pegdown" % "1.4.1" % "test",
  "junit" % "junit" % "4.11" % "test"
)

EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE17)

EclipseKeys.withSource := true

(testOptions in Test) += Tests.Argument(TestFrameworks.Specs2, "junitxml", "console", "html")

net.virtualvoid.sbt.graph.Plugin.graphSettings
