package kinoplaner

import org.joda.time.{DateTime, LocalDate, LocalTime}
import java.util.UUID


trait InMemoryKinoFixtureService extends KinoService {


  lazy val filmList = List(Film("Twoilette"), Film("Skyfall"))

  lazy val kinoList = List(Kino("Monpa", GPSCoord(48.843271D, 2.325165D)), Kino("01", GPSCoord(48.864049D, 2.331053D)))

  lazy val datetimes: List[DateTime] = {
    val times = List((12, 30), (16, 00), (19, 00), (22, 00)).map(t => new LocalTime(t._1, t._2))
    val dates = (1 to 30).map(i => new LocalDate(2012, 11, i))
    (for (t <- times; d <- dates) yield {
      d.toDateTime(t)
    })
  }


  lazy val showList: List[Showing] = for (f <- filmList;
                                          k <- kinoList;
                                          dt <- datetimes)
  yield {
    Showing(f, k, dt)
  }


  def listFilms() = filmList

  def listKinos() = kinoList

  def findKino(name: String) = kinoList.filter(_.name == name)

  def listShowing() = showList
}


trait InMemoryKplanerCreationService extends KPlanerCreationService {
  def newKplaner(kino: Kino, date: DateTime) = {
    Kplaner(UUID.randomUUID().toString, kino, date, Set.empty)
  }
}


