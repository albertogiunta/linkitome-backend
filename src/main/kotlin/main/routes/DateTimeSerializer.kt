package main.routes

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.bson.types.ObjectId
import org.joda.time.DateTime

class DateTimeSerializer : StdSerializer<DateTime>(DateTime::class.java) {

    val yyyyMMddTHHmm00 = "yyyy-MM-dd'T'HH:mm:00"
    val yyyyMMddTHHmmssZ = "yyyy-MM-dd'T'HH:mm:ss:Z"

    override fun serialize(value: DateTime?, gen: JsonGenerator?, provider: SerializerProvider?) {
        // kotlin <3
        value?.let { gen?.writeString(value.toString(yyyyMMddTHHmmssZ)) } ?: gen?.writeNull()
    }
}

class ObjectIdSerializer : StdSerializer<ObjectId>(ObjectId::class.java) {

    override fun serialize(value: ObjectId?, gen: JsonGenerator?, provider: SerializerProvider?) {
        value?.let { gen?.writeString(value.toString()) } ?: gen?.writeNull()
    }

}