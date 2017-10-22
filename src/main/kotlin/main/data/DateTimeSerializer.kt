package main.data

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.bson.types.ObjectId
import org.joda.time.DateTime

class DateTimeSerializer : StdSerializer<DateTime>(DateTime::class.java) {

    private val yyyyMMddTHHmmSSZ = "yyyy-MM-dd'T'HH:mm:ss:Z"

    override fun serialize(value: DateTime?, gen: JsonGenerator?, provider: SerializerProvider?) {
        value?.let { gen?.writeString(value.toString(yyyyMMddTHHmmSSZ)) } ?: gen?.writeNull()
    }
}

class ObjectIdSerializer : StdSerializer<ObjectId>(ObjectId::class.java) {

    override fun serialize(value: ObjectId?, gen: JsonGenerator?, provider: SerializerProvider?) {
        value?.let { gen?.writeString(value.toString()) } ?: gen?.writeNull()
    }
}