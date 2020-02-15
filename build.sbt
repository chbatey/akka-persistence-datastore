val AkkaVersion = "2.6.3"

val dependencies = Seq(
  "com.spotify"             % "async-datastore-client"              % "3.0.2",
  "com.typesafe.akka"      %% "akka-persistence-typed"              % AkkaVersion,
  "com.typesafe.akka"      %% "akka-persistence-query"              % AkkaVersion,
  "com.typesafe.akka"      %% "akka-persistence-tck"                % AkkaVersion     % "test",
  "com.typesafe.akka"      %% "akka-stream-testkit"                 % AkkaVersion     % "test",
  "ch.qos.logback"          % "logback-classic"                     % "1.2.3"         % "test",
  "org.scalatest"          %% "scalatest"                           % "3.1.0"         % "test"
)

lazy val root = (project in file("."))
  .settings(
    name := "akka-persistence-datastore",
    libraryDependencies ++= dependencies,
    scalaVersion := "2.13.1",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
  )
