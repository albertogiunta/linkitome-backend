package main.routes

import main.model.Digest
import main.model.Link
import main.model.crud.DigestRepository
import main.model.crud.LinkRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class CollectionController {

    @Autowired
    lateinit var digestRepository: DigestRepository

    @Autowired
    lateinit var linkRepository: LinkRepository

    @GetMapping("/digests")
    fun getDigests(): List<Digest> = digestRepository.findAll()

    @DeleteMapping("/digests")
    fun removeDigests() = digestRepository.deleteAll()

    @PostMapping("/digests")
    fun newDigest(@RequestParam title: String): Digest = digestRepository.save(Digest(ObjectId(), title, mutableListOf()))

    @GetMapping("/digests/{digestId}")
    fun getDigestBydId(@PathVariable digestId: String): Digest = digestRepository.findOne(ObjectId(digestId))

    @DeleteMapping("/digests/{digestId}")
    fun removeDigestBydId(@PathVariable digestId: String) = digestRepository.delete(ObjectId(digestId))

    @PutMapping("/digests/{digestId}")
    fun modifyDigestBydId(@PathVariable digestId: String, @RequestParam title: String) {
        val modifiedDigest = getDigestBydId(digestId)
        modifiedDigest.title = title
        digestRepository.save(modifiedDigest)
    }

    @GetMapping("/digests/{digestId}/links")
    fun getLinks(@PathVariable digestId: String): List<Link> = getDigestBydId(digestId).links

    @DeleteMapping("/digests/{digestId}/links")
    fun removeLinks(@PathVariable digestId: String) {
        val modifiedDigest = getDigestBydId(digestId)
        modifiedDigest.links.clear()
        digestRepository.save(modifiedDigest)
    }

    @PostMapping("/digests/{digestId}/links")
    fun newLink(@PathVariable digestId: String, @RequestParam title: String){
        val modifiedDigest: Digest = getDigestBydId(digestId)
        modifiedDigest.links.add(Link(ObjectId(), title))
        digestRepository.save(modifiedDigest)
    }

    @GetMapping("/digests/{digestId}/links/{linkId}")
    fun getLinkBydId(@PathVariable digestId: String, @PathVariable linkId: String): Link =
            getDigestBydId(digestId).links.first { l -> l.id == ObjectId(linkId) }

    @DeleteMapping("/digests/{digestId}/links/{linkId}")
    fun removeLinkBydId(@PathVariable digestId: String, @PathVariable linkId: String) {
        val modifiedDigest: Digest = getDigestBydId(digestId)
        modifiedDigest.links.removeIf { l -> l.id == ObjectId(linkId) }
        digestRepository.save(modifiedDigest)
    }

    @PutMapping("/digests/{digestId}/links/{linkId}")
    fun modifyLink(@PathVariable digestId: String, @PathVariable linkId: String, @RequestParam title: String) {
        val modifiedDigest: Digest = getDigestBydId(digestId)
        modifiedDigest.links.filter { l -> l.id == ObjectId(linkId) }.map { l -> l.title = title }.toList()
        digestRepository.save(modifiedDigest)
    }
}