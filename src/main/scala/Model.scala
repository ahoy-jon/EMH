package kinoplaner

import org.joda.time.DateTime

case class Showing(film:Film, kino:Kino, datetime:DateTime)

case class GPSCoord(lat:Double, lng:Double)

case class Film(name:String)

case class Kino(name:String, gps:GPSCoord)

case class Kplaner(id:String, kino:Kino, date:DateTime, anwsers:Set[KplanerRow])


/**
 * @param name participant name
 */
case class KplanerRow(name:String, answers:Map[Showing,Boolean])