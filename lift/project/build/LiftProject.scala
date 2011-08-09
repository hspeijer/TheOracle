import sbt._

class LiftProject(info: ProjectInfo) extends DefaultWebProject(info) {
  val liftVersion = "2.3"

  // uncomment the following if you want to use the snapshot repo
  //  val scalatoolsSnapshot = ScalaToolsSnapshots

  val mirror = "Antelink" at "http://maven.antelink.com/content/repositories/central/"

  // If you're using JRebel for Lift development, uncomment
  // this line
  // override def scanDirectories = Nil

  override def libraryDependencies = Set(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
    "net.liftweb" %% "lift-mapper" % liftVersion % "compile",
    "net.liftweb" %% "lift-widgets" % liftVersion % "compile",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test",
    "junit" % "junit" % "4.5" % "test",
    "ch.qos.logback" % "logback-classic" % "0.9.26",
    "org.scala-tools.testing" %% "specs" % "1.6.6" % "test",
    "com.h2database" % "h2" % "1.2.138",
    "org.neo4j" % "neo4j" % "1.3",
    "org.neo4j" % "neo4j-index" % "1.2-1.2",
    "log4j" % "log4j" % "1.2.9",
    "org.slf4j" % "slf4j-log4j12" % "1.6.1"
	
  ) ++ super.libraryDependencies

  override def extraWebappFiles = (sourcePath / "main" ##) / "resources" ** "*"

}
