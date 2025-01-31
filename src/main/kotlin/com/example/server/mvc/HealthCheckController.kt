package com.example.server.mvc

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {

    @GetMapping("/healthcheck")
    fun healthCheck(): ResponseEntity<String>? {
        return ResponseEntity.ok("ok")
    }
}
