package main.routes

import main.model.*
import main.model.crud.DigestRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class DigestController {

    @Autowired
    lateinit var digestRepository: DigestRepository

    @GetMapping("/digests")
    fun getDigests(): List<Digest> = digestRepository.findAll()

    @DeleteMapping("/digests")
    fun removeDigests() = digestRepository.deleteAll()

    @PostMapping("/digests")
    fun newDigest(@RequestParam title: String): Digest = digestRepository.save(Digest(ObjectId(), title))

    @GetMapping("/digests/{digestId}")
    fun getDigestBydId(@PathVariable digestId: String): Digest {
        return digestRepository.findOne(ObjectId(digestId)) ?: throw DigestNotFoundException()
    }

    @DeleteMapping("/digests/{digestId}")
    fun removeDigestBydId(@PathVariable digestId: String) = digestRepository.delete(ObjectId(digestId))

    @PutMapping("/digests/{digestId}")
    fun modifyDigestBydId(@PathVariable digestId: String, @RequestParam title: String): Digest {
        val modifiedDigest = getDigestBydId(digestId)
        modifiedDigest.setNewDigestParameters(title)
        return modifiedDigest.saveToDb()
    }

    @GetMapping("/digests/{digestId}/links")
    fun getLinks(@PathVariable digestId: String): List<Link> = getDigestBydId(digestId).links

    @DeleteMapping("/digests/{digestId}/links")
    fun removeLinks(@PathVariable digestId: String): Digest {
        val modifiedDigest = getDigestBydId(digestId)
        modifiedDigest.removeAllLinks()
        return modifiedDigest.saveToDb()
    }

    @PostMapping("/digests/{digestId}/links")
    fun newLink(@PathVariable digestId: String, @RequestParam title: String): Digest {
        val modifiedDigest: Digest = getDigestBydId(digestId)
        modifiedDigest.addNewLink(title)
        return modifiedDigest.saveToDb()
    }

    @GetMapping("/digests/{digestId}/links/{linkId}")
    fun getLinkBydId(@PathVariable digestId: String, @PathVariable linkId: String): Link =
            getDigestBydId(digestId).getLinkById(linkId)

    @DeleteMapping("/digests/{digestId}/links/{linkId}")
    fun removeLinkBydId(@PathVariable digestId: String, @PathVariable linkId: String): Digest {
        val modifiedDigest: Digest = getDigestBydId(digestId)
        modifiedDigest.removeLinkById(linkId)
        return modifiedDigest.saveToDb()
    }

    @PutMapping("/digests/{digestId}/links/{linkId}")
    fun modifyLink(@PathVariable digestId: String, @PathVariable linkId: String, @RequestParam title: String): Digest {
        val modifiedDigest: Digest = getDigestBydId(digestId)
        modifiedDigest.setNewLinkParameters(linkId, title)
//        modifiedDigest.links.filter { l -> l.id == ObjectId(linkId) }.map { l -> l.title = title }.toList()
        return modifiedDigest.saveToDb()
    }


    private fun Digest.saveToDb(): Digest {
        return digestRepository.save(this)
    }

}

