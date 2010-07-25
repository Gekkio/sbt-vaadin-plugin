sbt-vaadin-plugin
=================

Features
--------

+ Generates Vaadin widget sets
+ Works with jetty-run
+ Includes generated files properly into SBT-generated WAR-files
+ Fully configurable through method overrides

Usage
-----

**Make sure you have the required GWT dependencies included.**
You'll need these (2.0.4 is just an example, feel free to use any version):

    val gwtDev = "com.google.gwt" % "gwt-dev" % "2.0.4"
    val gwtUser = "com.google.gwt" % "gwt-user" % "2.0.4" 
    

First, add the maven repository and the plugin declaration to project/plugins/Plugins.scala:

    import sbt._

    class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
      val jawsyMavenReleases = "Jawsy.fi M2 releases" at "http://oss.jawsy.fi/maven2/releases"
      val vaadinPlugin = "fi.jawsy" % "sbt-vaadin-plugin" % "0.1.0"
    }

Then mix the plugin into your project definition and override the widget set name:

    import fi.jawsy.sbtplugins.vaadin.VaadinPlugin
    import sbt._

    class SomeProject(info: ProjectInfo) extends DefaultWebProject(info) with VaadinPlugin {
      override def vaadinWidgetSet = "mypackage.MyWidgetSet"
    }

Add a corresponding module definition file into src/main/resources (in this example src/main/resources/mypackage/MyWidgetSet.gwt.xml):

    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE module PUBLIC
      "-//Google Inc.//DTD Google Web Toolkit 1.7.0//EN"
      "http://google-web-toolkit.googlecode.com/svn/tags/1.7.0/distro-source/core/src/gwt-module.dtd">
    <module>
    </module>

Add your widget set init parameter to web.xml:

    <web-app>
      <!-- Other web.xml stuff -->
      <servlet>
        <servlet-name>Vaadin</servlet-name>
        <servlet-class>com.vaadin.terminal.gwt.server.ApplicationServlet</servlet-class>
        <!-- Other Vaadin servlet parameters -->
        <init-param>
          <param-name>widgetset</param-name>
          <param-value>mypackage.MyWidgetSet</param-value>
        </init-param>
    </web-app>

The plugin compiles the widget set automatically if it hasn't been compiled before, so you can now run the webapp with jetty-run or package it with package.

Supported SBT actions
-----------

*   vaadin-compile

    > Compiles the Vaadin widgetset.

*   vaadin-clean

    > Cleans all generated Vaadin-specific files.
