sbt-vaadin-plugin
=================

## Introduction

sbt-vaadin-plugin is a plugin for [Simple Build Tool](http://code.google.com/p/simple-build-tool/) that generates widget sets for for [Vaadin](http://www.vaadin.com). If you intend to use any third party widgets not included in the core Vaadin distribution, you'll ned widget set compilation.

## Features

+ Generates Vaadin widget sets
+ Works with jetty-run
+ Includes generated files properly into SBT-generated WAR-files
+ Fully configurable through method overrides
+ Uses `java` command from PATH, not necessarily the same JVM you are running SBT with!

## Usage

**Make sure you have the required GWT dependencies included.**
You'll need these (2.0.4 is just an example, feel free to use any version):

    val gwtDev = "com.google.gwt" % "gwt-dev" % "2.0.4"
    val gwtUser = "com.google.gwt" % "gwt-user" % "2.0.4" 

First, add the maven repository and the plugin declaration to project/plugins/Plugins.scala:

    import sbt._

    class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
      val jawsyMavenReleases = "Jawsy.fi M2 releases" at "http://oss.jawsy.fi/maven2/releases"
      val vaadinPlugin = "fi.jawsy" % "sbt-vaadin-plugin" % "0.1.1"
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
      "-//Google Inc.//DTD Google Web Toolkit 2.0.4//EN"
      "http://google-web-toolkit.googlecode.com/svn/tags/2.0.4/distro-source/core/src/gwt-module.dtd">
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

### How do I ...?

#### Disable vaadin widget set compilation

`override autorunVaadinCompile = false`

#### Change GWT compiler arguments

`override vaadinCompilerArgs = super.vaadinCompilerArgs ::: List("-draftCompile", "-localWorkers", "2")`

#### Change GWT compiler JVM arguments

If you want to replace default arguments:
`override vaadinCompilerJvmArgs = List("-server", "-Xmx256M")`

If you just want add some arguments in addition to the defaults:
`override vaadinCompilerJvmArgs = super.vaadinCompilerJvmArgs ::: List("-server", "-XX:MaxPermSize:256m")`

## Supported SBT actions

*   vaadin-compile

    > Compiles a Vaadin widgetset.

## TODO

Nothing at the moment! _Please report any bugs you find_
