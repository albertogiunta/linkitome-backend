package main.data

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import main.exceptions.LinkNotFoundException
import main.exceptions.LinkURLNotValidException
import org.apache.commons.validator.routines.UrlValidator
import org.bson.types.ObjectId
import org.joda.time.DateTime
import org.jsoup.Jsoup
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.net.MalformedURLException
import java.net.UnknownHostException
import java.util.*

data class Link(
        @JsonSerialize(using = ObjectIdSerializer::class)
        var id: ObjectId = ObjectId(),
        var title: String = "",
        var comment: String = "",
        var url: String,
        @JsonSerialize(using = DateTimeSerializer::class)
        var creationTime: DateTime = DateTime.now(),
        @JsonSerialize(using = DateTimeSerializer::class)
        var updateTime: DateTime = DateTime.now()
)

object LinkFactory {

    private val validator = UrlValidator(arrayOf("http", "https"))
    private lateinit var document: org.jsoup.nodes.Document

    fun createNewLink(title: String?, comment: String?, url: String): Link {
        document = checkWebsiteProtocol(url)
        val newTitle = title ?: document.title()
        val newURL = document.location()
        return Link(title = newTitle, comment = comment ?: "", url = newURL)
    }

    private fun checkWebsiteProtocol(url: String): org.jsoup.nodes.Document {
        val newUrl = if (validator.isValid(url)) url else "http://$url"
        try {
            return Jsoup.connect(newUrl).get()
        } catch (e: UnknownHostException) {
            throw LinkURLNotValidException(url)
        } catch (e: MalformedURLException) {
            throw LinkURLNotValidException(url)
        }
    }
}

@Document(collection = "digest")
data class Digest(
        @Id
        @JsonSerialize(using = ObjectIdSerializer::class)
        val id: ObjectId = ObjectId(),
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

fun Digest.addNewLink(title: String?, comment: String?, url: String) {
    // TODO
    this.links.add(LinkFactory.createNewLink(title, comment, url))
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

fun Digest.setNewLinkParameters(linkId: String, newTitle: String) {
    this.updateTime = DateTime.now()
    val linkIdObj = ObjectId(linkId)
    this.links.forEach({
        if (it.id == linkIdObj) {
            it.title = newTitle
            return
        }
    })
}
