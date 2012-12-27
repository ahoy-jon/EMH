package kplaner

import kinoplaner._
import unfiltered.request._
import unfiltered.response._

import unfiltered.response.Html
import unfiltered.response.ResponseString
import org.joda.time.format.ISODateTimeFormat

import common._
import org.joda.time.DateTime

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

      def nLookup[E](key: String): QueryM[E,Option[List[String]]] =
        QueryM {
          (params, _, log0) =>
            (Some(key), log0, params.get(key).map(_.toList))
        }

      def isValid2ElementList[E](e:List[String] => E) = watch((os:Option[List[String]]) => {
        os match {
          case Some(a :: b :: Nil) => os
          case _ => None
        }
      }, e)

      def lift[E,A,B](r:Reporter[E,A,B]):Reporter[E,List[A],List[B]] = {
        (ola :Option[List[A]]) => {
          ola match  {
            case Some(l) => {
              val applied = l.map(a => r(Option(a)))
              if (applied.forall(_.isRight)) {
                Right(Option(applied.flatMap(_.right.get)))
              } else {
                Left(applied.flatMap(_.left.toOption).head)
              }
            }
            case None => Right(None)
          }
        }
      }

      val expected = for (
        test <- nLookup("ahoy") is isValid2ElementList(_ + "is not a valid list") is required("missing");
        test2 <- nLookup("ahoy2") is lift(isDate(_ + "not a date")) is  required("missing");
        kino <- lookup("kinoid") is isKino(_ + " is not a kino") is required("missing");
        date <- lookup("date") is isDate(_ + " is not a date") is required("missing")
      ) yield {
        val ahoy:List[String] = test.get
        val ahoy2:List[DateTime] = test2.get
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
