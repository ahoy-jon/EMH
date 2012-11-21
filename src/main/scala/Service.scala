package kinoplaner

import org.joda.time.DateTime
import java.util.UUID


trait KinoService/*[M[_]]*/ {
  def listFilms():/*M[*/List[Film]/*]*/
  def listKinos():List[Kino]
  def findKino(name:String): List[Kino]
  def listShowing():List[Showing]
}


trait KPlanerCreationService {
  def newKplaner(kino:Kino, date:DateTime):Kplaner
}
