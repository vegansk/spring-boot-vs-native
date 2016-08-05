name := "native-web-app"

version := "0.1.0"

scalaVersion := "2.11.8"

val akkaV = "2.3.9"
val sprayV = "1.3.3"

libraryDependencies ++= Seq(
  "io.spray"            %%  "spray-can"     % sprayV,
  "io.spray"            %%  "spray-routing" % sprayV,
  "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
  "com.lihaoyi"         %%  "upickle"       % "0.4.1",
  "com.typesafe.slick"  %%  "slick"         % "3.1.1",
  "org.slf4j"           %   "slf4j-nop"     % "1.6.4"
)

Revolver.settings
