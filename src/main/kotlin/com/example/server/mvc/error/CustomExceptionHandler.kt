package com.example.server.mvc.error

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.*
import javax.servlet.http.HttpServletResponse


@ControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(e: ApplicationException): ResponseEntity<*> {
        val errorMessage: HashMap<String, String> = HashMap()
        errorMessage["error"] = e.message
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(errorMessage)
    }

}