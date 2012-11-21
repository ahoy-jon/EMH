organization := "com.example"

name := "kplaner"

version := "0.1.0-SNAPSHOT"

libraryDependencies ++= {
  val uv = "0.6.4"
  Seq(
    "net.databinder" %% "unfiltered-filter" % uv,
    "net.databinder" %% "unfiltered-jetty"  % uv,
    "net.databinder" %% "unfiltered-json"   % uv,
    //"net.databinder" %% "unfiltered-scalate" %uv,
    //real datetime API
    "joda-time" % "joda-time" % "2.1",
    "org.joda" % "joda-convert" % "1.2",
    //json serialization
    "io.backchat.jerkson" % "jerkson_2.9.2" % "0.7.0",
    //database mapping
    "org.scalaquery" % "scalaquery_2.9.0-1" % "0.9.5",
    //a zero install database, you should use your fav here
    "com.h2database" % "h2" % "1.2.140",
    "net.databinder" %% "unfiltered-spec"   % uv % "test"
  )
}

resolvers ++= Seq(
  "java m2" at "http://download.java.net/maven/2"
)
