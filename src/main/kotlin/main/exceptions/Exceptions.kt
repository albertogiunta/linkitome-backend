package main.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Digest ID not found")
class DigestNotFoundException : Exception("Digest ID not found")

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Link ID not found")
class LinkNotFoundException : Exception("Link ID not found")

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Link URL not valid")
class LinkURLNotValidException(url: String) : Exception("Link URL not valid: $url")