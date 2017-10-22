package main.data

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface DigestRepository : MongoRepository<Digest, ObjectId>, DigestRepositoryCustom

interface DigestRepositoryCustom

class DigestRepositoryCustomImpl : DigestRepositoryCustom

/* ****************************************************************************************************************** */

interface LinkRepository : MongoRepository<Link, ObjectId>, LinkRepositoryCustom

interface LinkRepositoryCustom

class LinkRepositoryCustomImpl : LinkRepositoryCustom
