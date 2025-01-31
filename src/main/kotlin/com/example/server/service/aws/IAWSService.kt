package com.example.server.service.aws

import com.fasterxml.jackson.databind.JsonNode
import java.net.URI


interface IAWSService {
    fun savePicture(content: ByteArray, fileName: String, contentType: String): URI?
    fun sendEmail(toAddress: String?, content: String, subject: String)
    fun sendSms(toPhone: String?, content: String)
    fun getLocalization(language: String): JsonNode?
    fun getValue(secretName: String?): JsonNode?
}