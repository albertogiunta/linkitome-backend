package main

import main.controller.DigestController
import main.data.LinkFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@FixMethodOrder(MethodSorters.JVM)
class RESTTest {

    @Autowired
    var controller: DigestController = DigestController()

    var digestObjectId: String = ""
    var linkObjectId: String = ""

    val nDigests = 3
    val nLinks = 3

    @Test
    fun checkEmptyDB() {
        controller.removeDigests()
        assertThat(controller.getDigests().size).isEqualTo(0)
    }

    @Test
    fun checkPOSTofDigests() {
        (0 until nDigests).forEach {
            controller.newDigest("Digest $it")
        }
        assertThat(controller.getDigests().size).isEqualTo(nDigests)
    }

    @Test
    fun checkGETofDigest() {
        setDigestObjectId()
        assertThat(controller.getDigestBydId(digestObjectId).title).isEqualTo("Digest 0")
    }

    @Test
    fun checkPUTofDigest() {
        setDigestObjectId()
        controller.modifyDigestBydId(digestObjectId, "Modified Digest 1")
        assertThat(controller.getDigests()[0].id.toString()).isEqualTo(digestObjectId)
        assertThat(controller.getDigests()[0].title).isEqualTo("Modified Digest 1")
    }

    @Test
    fun checkDELETEofDigest() {
        setDigestObjectId(1)
        controller.removeDigestBydId(digestObjectId)
        assertThat(controller.getDigests().size).isEqualTo(nDigests - 1)
    }

    @Test
    fun checkPOSTofLinksInDigest() {
        setDigestObjectId()
        (0 until nLinks).forEach {
            controller.newLink(digestObjectId, "Link $it", "", "https://www.google.it")
        }
        assertThat(controller.getLinks(digestObjectId).size).isEqualTo(nLinks)
    }

    @Test
    fun checkGETofLinkInDigest() {
        setLinkAndDigestObjectId()
        assertThat(controller.getLinkBydId(digestObjectId, linkObjectId).title).isEqualTo("Link 0")
    }

    @Test
    fun checkPUTofLinkInDigest() {
        setLinkAndDigestObjectId()
        controller.modifyLink(digestObjectId, linkObjectId, "Modified Link 1")
        assertThat(controller.getLinkBydId(digestObjectId, linkObjectId).id.toString()).isEqualTo(linkObjectId)
        assertThat(controller.getLinkBydId(digestObjectId, linkObjectId).title).isEqualTo("Modified Link 1")
    }

    @Test
    fun checkDELETEofLinkInDigest() {
        setLinkAndDigestObjectId(1)
        controller.removeLinkBydId(digestObjectId, linkObjectId)
        assertThat(controller.getLinks(digestObjectId).size).isEqualTo(nLinks - 1)
    }

    @Test
    fun checkTitleFetchingInLinkFactory() {
        var link = LinkFactory.createNewLink(null, null, url = "www.google.it")
        assertThat(link.title).isEqualTo("Google")
        link = LinkFactory.createNewLink(null, null, url = "google.it")
        assertThat(link.title).isEqualTo("Google")
        link = LinkFactory.createNewLink(null, null, url = "albertogiunta.it")
        assertThat(link.title).isEqualTo("Alberto Giunta")
        link = LinkFactory.createNewLink(null, null, url = "http://www.facebook.it")
        assertThat(link.title).isEqualTo("Facebook: accedi o iscriviti")

    }

    private fun setDigestObjectId(index: Int = 0) {
        digestObjectId = controller.getDigests()[index].id.toString()
    }

    private fun setLinkAndDigestObjectId(index: Int = 0) {
        setDigestObjectId()
        linkObjectId = controller.getLinkBydId(digestObjectId, controller.getDigestBydId(digestObjectId).links[index].id.toString()).id.toString()
    }

}