package main.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "digest") data class Digest(@Id val id: ObjectId, var title: String, var links: MutableList<Link>)

@Document(collection = "digest") data class Link(@Id val id: ObjectId, var title: String)
