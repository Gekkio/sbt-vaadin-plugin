package fi.jawsy.sbtplugins.vaadin

import sbt._

object VaadinPlugin {

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

  private def compileVaadinWidgetSet {
    import Process._
    val cp = vaadinCompilerClasspath.getPaths.mkString(System.getProperty("path.separator"))
    val parts = "java" :: vaadinCompilerJvmArgs ::: List("-classpath", cp, vaadinCompilerClass) ::: vaadinCompilerArgs
    parts.mkString(" ") ! log
  }

  lazy val vaadinCompile = vaadinCompileAction
  def vaadinCompileAction = vaadinCompileTask.dependsOn(copyResources) describedAs VaadinCompileDescription
  def vaadinCompileTask = task {
    compileVaadinWidgetSet
    None
  }

  def autoVaadinCompileTask = task {
    if (autorunVaadinCompile) compileVaadinWidgetSet
    None
  } describedAs VaadinCompileDescription

  override def prepareWebappAction = super.prepareWebappAction dependsOn(autoVaadinCompileTask)

  override def extraWebappFiles = super.extraWebappFiles +++ descendents(vaadinCompilerOutputPath ##, "*")

}
