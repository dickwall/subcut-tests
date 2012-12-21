name := "subcut-tests"

organization := "com.escalatesoft.subcut"

version := "2.0-SNAPSHOT"

crossScalaVersions := Seq("2.10.0", "2.9.2", "2.9.1", "2.9.0-1", "2.9.0")

scalaVersion := "2.10.0"

scalacOptions += "-deprecation"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.8" % "test" cross CrossVersion.full

libraryDependencies += "junit" % "junit" % "4.5" % "test"

libraryDependencies += "com.escalatesoft.subcut" %% "subcut" % "2.0-SNAPSHOT"

libraryDependencies <<= (scalaVersion, libraryDependencies) { (ver, deps) =>
  deps :+ "org.scala-lang" % "scala-compiler" % ver
}

addCompilerPlugin("com.escalatesoft.subcut" %% "subcut" % "2.0-SNAPSHOT")
