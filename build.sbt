name := "scala_snippet"

version := "0.0"

scalaVersion := "2.10.2"

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature", "-P:continuations:enable")

javacOptions ++= Seq("-encoding", "UTF-8", "-source", "1.7", "-target", "1.7")

libraryDependencies ++= Seq(
  //"org.apache.solr" % "solr-core" % "4.4.0" excludeAll(ExclusionRule(organization = "org.restlet.jee")),
  "com.google.guava" % "guava" % "14.0.1",
  "org.specs2" %% "specs2" % "2.1.1" % "test",
  "org.pegdown" % "pegdown" % "1.4.1" % "test",
  "junit" % "junit" % "4.11" % "test"
)

EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE17)

EclipseKeys.withSource := true

(testOptions in Test) += Tests.Argument(TestFrameworks.Specs2, "junitxml", "console", "html")
