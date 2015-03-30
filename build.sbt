name := "scala_snippet"

version := "0.0"

scalaVersion := "2.11.6"

//scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature", "-P:continuations:enable")

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature")

javacOptions ++= Seq("-encoding", "UTF-8", "-source", "1.8", "-target", "1.8")

libraryDependencies ++= Seq(
  //"org.apache.solr" % "solr-core" % "4.4.0" excludeAll(ExclusionRule(organization = "org.restlet.jee")),
  "org.jruby" % "jruby" % "1.7.4",
  "org.codehaus.groovy" % "groovy-jsr223" % "2.1.6",
  "com.itextpdf" % "itextpdf" % "5.4.3",
  "com.itextpdf" % "itext-asian" % "5.2.0",
  "org.scalaz" %% "scalaz-core" % "7.1.1",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3",
  "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3",
  "com.google.guava" % "guava" % "18.0",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.5",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  //"org.specs2" %% "specs2" % "3.2" % "test",
  //"org.pegdown" % "pegdown" % "1.4.1" % "test",
  "junit" % "junit" % "4.11" % "test",
  "net.sf.barcode4j" % "barcode4j" % "2.1",
  "net.sourceforge.barbecue" % "barbecue" % "1.5-beta1",
  "com.github.nscala-time" %% "nscala-time" % "1.8.0"
)

EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE18)

EclipseKeys.withSource := true

(testOptions in Test) += Tests.Argument(TestFrameworks.Specs2, "junitxml", "console", "html")
