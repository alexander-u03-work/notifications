package ru.raggies.notification.exceptions

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class CustomExceptionsHandler(
    private val objectMapper: ObjectMapper,
): ResponseEntityExceptionHandler() {

    companion object {
        private val log = LoggerFactory.getLogger(CustomExceptionsHandler::class.java)!!
    }

    @ExceptionHandler(Throwable::class)
    fun handlerErrors(request: WebRequest, t: Throwable): ResponseEntity<Any>? {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        log.error("Exception while do operation", t)

        return handleExceptionInternal(
            RuntimeException(t),
            objectMapper.writeValueAsBytes(Error(t.message)),
            headers,
            HttpStatus.BAD_REQUEST,
            request
        )
    }

    data class Error(
        val message: String?
    )

}