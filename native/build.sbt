name := "native-web-app"

version := "0.1.0"

scalaVersion := "2.11.8"

val akkaV = "2.3.9"
val sprayV = "1.3.3"
val scalikejdbcV = "2.4.2"

libraryDependencies ++= Seq(
  "io.spray"            %%  "spray-can"     % sprayV,
  "io.spray"            %%  "spray-routing" % sprayV,
  "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
  "com.lihaoyi"         %%  "upickle"       % "0.4.1",
  "org.scalikejdbc"     %%  "scalikejdbc"   % scalikejdbcV,
  "org.scalikejdbc"     %%  "scalikejdbc-config"  % scalikejdbcV,
  "org.scalikejdbc"     %%  "scalikejdbc-syntax-support-macro" % scalikejdbcV,
  "com.h2database"      %   "h2" % "1.4.192",
  "ch.qos.logback"      %   "logback-classic" % "1.1.7"
)

Revolver.settings
