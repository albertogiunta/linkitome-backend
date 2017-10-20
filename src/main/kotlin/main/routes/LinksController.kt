package main.routes

import main.model.crud.DigestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class LinksController {

    @Autowired
    lateinit var repository: DigestRepository

}