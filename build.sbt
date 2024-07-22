ThisBuild / scalaVersion := "3.4.2"

val catsEffectVersion = "3.5.4"
val weaverVersion = "0.8.3"

lazy val cats = (project in file("cats"))
  .settings(
    name := "flogger-cats",
    libraryDependencies ++= {
      Seq(
        "org.typelevel" %% "cats-effect" % catsEffectVersion,
      )
    },
  ).dependsOn(core)

lazy val weaver = (project in file("weaver"))
  .settings(
    name := "flogger-weaver",
    libraryDependencies ++= {
      Seq(
        "org.typelevel" %% "cats-effect" % catsEffectVersion,
        "com.disneystreaming" %% "weaver-core" % weaverVersion,
      )
    },
  ).dependsOn(core)


lazy val core = (project in file("core"))
  .settings(
    name := "flogger",
    libraryDependencies ++= {
      Seq(
        "org.typelevel" %% "cats-effect-kernel" % catsEffectVersion,
        "org.slf4j" % "slf4j-api" % "2.0.13",
      )
    }
  )


lazy val root = (project in file("."))
  .settings(
    name := "flogger-root-nopublish",
    libraryDependencies ++= Seq(
      "com.disneystreaming" %% "weaver-cats" % weaverVersion % Test,
      "ch.qos.logback" % "logback-classic" % "1.5.6" % Test,
    ),
 
    testFrameworks += new TestFramework("weaver.framework.CatsEffect"),
    // fork := true,
  ).dependsOn(core, cats, weaver)
