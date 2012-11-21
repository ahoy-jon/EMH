package kinoplaner

import kinoplaner.{InMemoryKplanerCreationService, InMemoryKinoFixtureService}
import kplaner.KApp
import org.specs._


object ExampleSpec extends Specification with unfiltered.spec.jetty.Served {
  
  import dispatch._
  
  def setup = { _.filter(new KApp with InMemoryKinoFixtureService with InMemoryKplanerCreationService) }
  

  val http = new Http
  
  "Kapp" should {
    "404 on /" in {
      val status = http x (host as_str) {
        case (code, _, _, _) => code
      }
      status must_== 404
    }

    "200 on kinolist" in {
      val status = http x (host / "kinolist" as_str) {
        case (code, _, _, _ ) => code
      }
      status must_==200
    }
  }

}