val catsEffectVersion = "3.5.4"
val weaverVersion = "0.8.3"

lazy val cats = (project in file("cats"))
  .settings(ScalaProject.publicProjectSettings)
  .settings(
    name := "flogger-cats",
    libraryDependencies ++= {
      Seq(
        "org.typelevel" %% "cats-effect" % catsEffectVersion,
      )
    },
  ).dependsOn(core)

lazy val weaver = (project in file("weaver"))
  .settings(ScalaProject.publicProjectSettings)
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
  .settings(ScalaProject.publicProjectSettings)
  .settings(
    name := "flogger",
    libraryDependencies ++= {
      Seq(
        "org.typelevel" %% "cats-effect-kernel" % catsEffectVersion,
        "org.slf4j" % "slf4j-api" % "2.0.13",
      )
    }
  )

lazy val bench = (project in file("bench"))
  .settings(ScalaProject.hiddenProjectSettings)
  .settings(
    name := "flogger-bench-internal",
    libraryDependencies ++= {
      Seq(
        "org.typelevel" %% "cats-effect-kernel" % catsEffectVersion,
        "org.slf4j" % "slf4j-api" % "2.0.13",
        "org.typelevel" %% "log4cats-slf4j" % "2.7.0",
        "ch.qos.logback" % "logback-classic" % "1.5.6",
      )
    },
  ).enablePlugins(JmhPlugin)
  .dependsOn(cats)

lazy val root = (project in file("."))
  .settings(ScalaProject.hiddenProjectSettings)
  .settings(
    name := "flogger-root-internal",
    libraryDependencies ++= Seq(
      "com.disneystreaming" %% "weaver-cats" % weaverVersion % Test,
      "ch.qos.logback" % "logback-classic" % "1.5.6" % Test,
    ),
    addCommandAlias("benchmark",     "bench / compile; bench / jmh:run -i 2 -wi 1 -w 1 -f 1 -t 1 -r 1 -tu ms -rf text -rff bench.txt"),
    addCommandAlias("benchmarkFull", "bench / compile; bench / jmh:run -i 5 -wi 3 -w 5 -f 1 -t 1 -r 10 -tu ms -rf text -rff bench-full.txt"),
 
    testFrameworks += new TestFramework("weaver.framework.CatsEffect"),
    // fork := true,
  ).dependsOn(core, cats, weaver)
  .aggregate(core, cats, weaver, bench)

ThisBuild / versionScheme := Some("semver-spec")
