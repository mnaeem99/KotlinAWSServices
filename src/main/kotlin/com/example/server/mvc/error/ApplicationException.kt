package com.example.server.mvc.error

class ApplicationException( override val message: String) : RuntimeException(message)