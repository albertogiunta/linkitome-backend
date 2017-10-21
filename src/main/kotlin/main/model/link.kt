package main.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import main.routes.DateTimeSerializer
import main.routes.LinkNotFoundException
import main.routes.ObjectIdSerializer
import org.bson.types.ObjectId
import org.joda.time.DateTime
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

data class Link(
        @JsonSerialize(using = ObjectIdSerializer::class)
        val id: ObjectId,
        var title: String,
        @JsonSerialize(using = DateTimeSerializer::class)
        var creationTime: DateTime = DateTime.now(),
        @JsonSerialize(using = DateTimeSerializer::class)
        var updateTime: DateTime = DateTime.now()
)

@Document(collection = "digest")
data class Digest(
        @Id
        @JsonSerialize(using = ObjectIdSerializer::class)
        val id: ObjectId,
        var title: String,
        @JsonSerialize(using = DateTimeSerializer::class)
        var creationTime: DateTime = DateTime.now(),
        @JsonSerialize(using = DateTimeSerializer::class)
        var updateTime: DateTime = DateTime.now(),
        var links: MutableList<Link> = mutableListOf()
)

fun Digest.setNewDigestParameters(title: String) {
    this.title = title
    this.updateTime = DateTime.now()
}

fun Digest.removeAllLinks() {
    this.links.clear()
    this.updateTime = DateTime.now()
}

fun Digest.addNewLink(title: String) {
    this.links.add(Link(ObjectId(), title))
    this.updateTime = DateTime.now()
}

fun Digest.getLinkById(linkId: String): Link {
    val linkIdObj = ObjectId(linkId)
    try {
        return this.links.first { l -> l.id == linkIdObj }
    } catch (e: NoSuchElementException) {
        throw LinkNotFoundException()
    }
}

fun Digest.removeLinkById(linkId: String) {
    this.updateTime = DateTime.now()
    val linkIdObj = ObjectId(linkId)
    this.links.removeIf { l -> l.id == linkIdObj }
}

fun Digest.setNewLinkParameters(linkId: String, title: String) {
    this.updateTime = DateTime.now()
    val linkIdObj = ObjectId(linkId)
    for (l: Link in this.links) {
        if (l.id == linkIdObj) {
            l.title = title
            l.updateTime = DateTime.now()
            break
        }
    }
}
