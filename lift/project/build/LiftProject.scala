import sbt._

class LiftProject(info: ProjectInfo) extends DefaultWebProject(info) {
  val liftVersion = "2.3"

  // uncomment the following if you want to use the snapshot repo
  //  val scalatoolsSnapshot = ScalaToolsSnapshots

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
    "org.neo4j" % "neo4j-index" % "1.2-1.2"
	
  ) ++ super.libraryDependencies

  //override val jettyPort = 80

  //TODO HSP 22-7-2011: Figure out how to reach the files in the WEB-INF classes folder
  override def extraWebappFiles = (sourcePath / "main" ##) / "resources" ** "*"

}
