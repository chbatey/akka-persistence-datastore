val AkkaVersion = "2.5.9"

val dependencies = Seq(
  "com.spotify"             % "async-datastore-client"              % "3.0.0",
  "com.typesafe.akka"      %% "akka-persistence"                    % AkkaVersion,
  "com.typesafe.akka"      %% "akka-persistence-query"              % AkkaVersion,
  "com.typesafe.akka"      %% "akka-persistence-tck"                % AkkaVersion     % "test",
  "com.typesafe.akka"      %% "akka-stream-testkit"                 % AkkaVersion     % "test",
  "ch.qos.logback"          % "logback-classic"                     % "1.2.3"         % "test",
  "org.scalatest"          %% "scalatest"                           % "3.0.4"         % "test"
)

lazy val root = (project in file("."))
  .settings(
    name := "akka-persistence-datastore",
    libraryDependencies ++= dependencies
  )
