//
// Main build file of tollgate project
//

ThisBuild / name         := "tollgate"
ThisBuild / organization := "io.tollgate"
ThisBuild / scalaVersion := "2.12.8"


ThisBuild / version      := "1.0-SNAPSHOT"
lazy val root = ( project in file(".") ).enablePlugins( PlayScala )


val playSlick           = "com.typesafe.play"      %% "play-slick"            % "3.0.0"
val playSlickEvolutions = "com.typesafe.play"      %% "play-slick-evolutions" % "4.0.0"
val h2                  = "com.h2database"         %  "h2"                    % "1.4.199"
val scalaTestPlus       = "org.scalatestplus.play" %% "scalatestplus-play"    % "4.0.1"

libraryDependencies ++= Seq(
  guice,
  h2,
  playSlick,
  playSlickEvolutions,
  scalaTestPlus % Test
)


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "io.tollgate.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "io.tollgate.binders._"


// val hikariCP  = "com.zaxxer"          %% "HikariCP"         % "2.7.8"
// val slick     = "com.typesafe.slick"  %% "slick"            % "3.3.0"
