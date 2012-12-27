import kinoplaner.KinoService

import org.scalaquery.ql.extended.{ExtendedTable => Table}
import org.scalaquery.session.Database


import org.scalaquery.session._
import org.scalaquery.session.Database.threadLocalSession

// Import the query language
import org.scalaquery.ql._

import org.scalaquery.ql.extended.H2Driver.Implicit._

// Import the standard SQL types
import org.scalaquery.ql.TypeMapper._

/**
 * Documentation : https://github.com/szeiger/scalaquery-examples/blob/master/src/main/scala/org/scalaquery/examples/FirstExample.scala
 */

class DBPersistance extends KinoService with LocalH2{
  import common.???

  def listFilms() = ???

  def listKinos() = ???
  /*  db withSession {
    Query(DBPersistance.Kinos)
  }*/
  def findKino(name: String) = ???

  def listShowing() = ???
}





trait DbProvider {
  def db:Database
}

trait LocalH2 extends DbProvider {
  def db = Database.forURL(url ="jdbc:h2:~/kplaner", driver = "org.h2.Driver")
}

trait MemoryH2 extends DbProvider {
  def db = Database.forURL(url = "jdbc:h2:mem:test2", driver = "org.h2.Driver")
}




object DBPersistance {



  lazy val Kinos = new Table[( String,Double,Double)]("KINO") {
    def name = column[String]("KINO_NAME", O.PrimaryKey) // This is the primary key column
    def lat = column[Double]("KINO_LAT")
    def long = column[Double]("KINO_LONG")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = name ~ lat ~ long
  }


}

object InsertFixture extends App with  LocalH2 {
  db withSession {
    DBPersistance.Kinos.insert("montparnasse", 48.843271D,2.325165D)
  }
}

object IniDb extends App with LocalH2 {
  db withSession {
    DBPersistance.Kinos.ddl.create
  }
}

object ResetDb extends App {


}