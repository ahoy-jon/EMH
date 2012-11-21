package kinoplaner

import com.codahale.jerkson.Json
import com.fasterxml.jackson.core.{Version, JsonGenerator}
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.{JsonDeserializer, SerializerProvider, JsonSerializer}
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import java.text.DateFormat
import java.util.Locale


class DateTimeSerializer extends JsonSerializer[DateTime] {
  def serialize(datetime: DateTime, json: JsonGenerator, provider: SerializerProvider) {
    json.writeString({
      val fmt = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US)
      fmt.format(datetime.toDate)
    })
  }
}

object CustomSerializer extends Json {
  val module = new SimpleModule("DateTimeSerializer", Version.unknownVersion())
  module.addSerializer(classOf[DateTime], new DateTimeSerializer)
  mapper.registerModule(module)
}