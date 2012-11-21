package kplaner

import kinoplaner._
import unfiltered.request._
import unfiltered.response._

import unfiltered.response.Html
import unfiltered.response.ResponseString
import org.joda.time.format.ISODateTimeFormat

import common._
/** unfiltered plan */
trait KApp extends unfiltered.filter.Plan {
  self: KinoService with KPlanerCreationService =>






  def jsonResponse[T](t:T)= JsonContent ~> ResponseString(CustomSerializer.generate(t))

  def intent = {
    case GET(Path("/kinolist")) => jsonResponse(listKinos())
    case GET(Path("/filmlist")) => NotImplemented
    case GET(Path("/sessions")) => NotImplemented


    case POST(Path("/kplaner") & Params(params)) => {
      import unfiltered.request.QParams._

      def isDate[E](e:String => E) = watch((os:Option[String]) => {
        os.flatMap(s => try {
          Option(ISODateTimeFormat.dateTimeParser().parseDateTime(s))
        } catch {
          case _:Exception => None
        })
      }, e)


      def isKino[E](e:String => E) = watch((os:Option[String]) => {
        os.flatMap(s => findKino(s).headOption)
      },e)

      val expected = for (
        kino <- lookup("kinoid") is isKino(_ + " is not a kino") is required("missing");
        date <- lookup("date") is isDate(_ + " is not a date") is required("missing")
      ) yield {
        Ok ~> JsContent ~> ResponseString(CustomSerializer.generate(newKplaner(kino.get, date.get)))
      }

      expected(params) orFail (failures => {
        BadRequest ~> JsContent ~> ResponseString(CustomSerializer.generate(failures))
      })

    }

/**
 *  Wanna try some HTML ? => https://github.com/unfiltered/unfiltered-scalate
 *  Just add
 */

    case Path("/help") => NotImplemented



    case _ => NotFound ~> HtmlContent ~> Html(<html><body><h1>NotFound</h1><ul>
      {List("kinolist", "filmlist", "sessions").map(s => <li><a href={s}>{s}</a></li>)}
    </ul>
    </body></html>)
  }
}

/** embedded server */
object Server {
  def main(args: Array[String]) {
    val http = unfiltered.jetty.Http(8080) // this will not be necessary in 0.4.0
    http.context("/assets") { _.resources(new java.net.URL(getClass().getResource("/www/css"), ".")) }
      .filter(new KApp with InMemoryKinoFixtureService with InMemoryKplanerCreationService).run({ svr =>
    {
      unfiltered.util.Browser.open(Egg.url)
      unfiltered.util.Browser.open(http.url)
    }
      }, { svr =>
        println("shutting down server")
      })
  }
}
