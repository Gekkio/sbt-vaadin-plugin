package fi.jawsy.sbtplugins.vaadin

import sbt._

object VaadinPlugin {

  val VaadinCleanDescription = "Deletes all generated Vaadin files"
  val VaadinCompileDescription = "Compiles a Vaadin widget set"

}

trait VaadinPlugin extends DefaultWebProject {

  import VaadinPlugin._

  def vaadinWidgetSet: String = "com.vaadin.terminal.gwt.DefaultWidgetSet"
  def vaadinOutputPath: Path = vaadinCompilerOutputPath / "VAADIN" / "widgetsets"

  def vaadinCompilerClass: String = "com.vaadin.tools.WidgetsetCompiler"
  def vaadinCompilerArgs: List[String] = List("-out", vaadinOutputPath.absolutePath, vaadinWidgetSet)
  def vaadinCompilerJvmArgs: List[String] = List("-Xmx128M")
  def vaadinCompilerClasspath: PathFinder = compileClasspath +++ mainResourcesOutputPath
  def vaadinCompilerOutputPath = outputPath / "vaadin"
  def autorunVaadinCompile = !vaadinCompilerOutputPath.exists

  def vaadinCompileAction = task {
    import Process._
    val cp = vaadinCompilerClasspath.getPaths.mkString(":")
    val parts = "java" :: vaadinCompilerJvmArgs ::: List("-classpath", cp, vaadinCompilerClass) ::: vaadinCompilerArgs
    parts.mkString(" ") ! log
    None
  } dependsOn(copyResources) describedAs VaadinCompileDescription

  lazy val vaadinCompile = vaadinCompileAction

  override def prepareWebappAction = {
    if (autorunVaadinCompile) super.prepareWebappAction dependsOn(vaadinCompileAction)
    else super.prepareWebappAction
  }

  override def extraWebappFiles = super.extraWebappFiles +++ descendents(vaadinCompilerOutputPath ##, "*")

}
