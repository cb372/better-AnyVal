scalaVersion := "2.11.8"

resolvers += Resolver.sonatypeRepo("snapshots")
autoCompilerPlugins := true

val circeVersion = "0.5.0-M3"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,

  "com.typesafe.play" %% "play-json" % "2.5.6",

  "org.scalameta" %% "scalameta" % "1.1.0-SNAPSHOT",
  compilerPlugin("org.scalameta" % "paradise_2.11.8" % "3.0.0-SNAPSHOT")
)
