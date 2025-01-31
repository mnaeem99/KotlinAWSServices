package com.example.server.service.aws

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.*
import com.amazonaws.services.secretsmanager.AWSSecretsManager
import com.amazonaws.services.secretsmanager.model.AWSSecretsManagerException
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.model.*
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.*
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.example.server.mvc.error.ApplicationException
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import java.io.*
import java.net.URI

@Service
@Validated
@Slf4j
class AWSService @Autowired constructor(
    private val s3:AmazonS3,
    private val sns:AmazonSNS,
    private val amazonSimpleEmailService: AmazonSimpleEmailService,
    @Value("\${aws.ses.email}")
    private var fromEmail:String,
    @Value("\${aws.s3.bucket}")
    private var bucketName:String,
    private val secretManager: AWSSecretsManager,
    @Value("\${aws.s3.website.localization}")
    private val fileName: String
) : IAWSService {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun savePicture(content: ByteArray, fileName: String, contentType: String): URI? {
        try {
            val metadata = ObjectMetadata()
            metadata.contentType = contentType
            val putObjectRequest = PutObjectRequest(bucketName, fileName, ByteArrayInputStream(content), metadata)
            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead)
            s3.putObject(putObjectRequest)
            return s3.getUrl(bucketName, fileName).toURI()
        } catch (e: AmazonS3Exception) {
            logger.error("Exception while uploading to s3 buckets: " + e.message)
            throw ApplicationException("Exception while retrieving s3 buckets: " + e.message)
        }
    }
    override fun sendEmail(toAddress: String?, content: String, subject: String) {
        try {
            val request = SendEmailRequest()
                .withDestination(
                    Destination().withToAddresses(toAddress)
                )
                .withMessage(
                    Message()
                        .withBody(
                            Body()
                                .withHtml(
                                    Content()
                                        .withCharset("UTF-8").withData(content)
                                )
                        )
                        .withSubject(
                            Content()
                                .withCharset("UTF-8").withData(subject)
                        )
                )
                .withSource(fromEmail)
            amazonSimpleEmailService.sendEmail(request)
            logger.info( "Email Message Successfully sent.")
        }catch (e: AmazonSimpleEmailServiceException){
            logger.error("Exception while sending email: " + e.message)
            throw ApplicationException("Exception while sending email: " + e.message)
        }
    }

    @Async
    override fun sendSms(toPhone: String?, content: String) {
        try {
            val result: PublishResult = sns.publish(
                PublishRequest()
                    .withMessage(content)
                    .withPhoneNumber(toPhone)
            )
            logger.info(result.messageId + " Message Successfully sent to "+toPhone)
        } catch (e: AmazonSNSException) {
            logger.error("Exception while sending sms: " + e.message)
            throw ApplicationException("Exception while sending sms: " + e.message)
        }
    }
    fun getObject(name: String): InputStream? {
        try {
            return s3.getObject(bucketName, name).objectContent
        } catch (e: AmazonS3Exception) {
            logger.error("Exception while retrieving s3 buckets: " + e.message)
            throw ApplicationException("Exception while retrieving s3 buckets: " + e.message)
        }
    }
    @Cacheable("localization", key = "#language")
    override fun getLocalization(language: String): JsonNode? {
        val s3Object = getObject(fileName)
        val mapper = ObjectMapper()
        try {
            val jsonNode = mapper.readValue(slurp(s3Object), JsonNode::class.java)
            return jsonNode?.get(language)
        } catch (_: IOException) {
            throw ApplicationException("Exception while fetching internationalization")
        }
    }
    fun slurp(inputStream: InputStream?): String? {
        if (inputStream == null)
            return ""
        val br = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
        try {
            var line = br.readLine()
            while (line != null) {
                sb.append(line)
                line = br.readLine()
            }
        } finally {
            br.close()
        }
        return sb.toString()
    }

    @Cacheable("secretManager", key="#secretName")
    override fun getValue(secretName: String?): JsonNode? {
        val objectMapper = ObjectMapper()
        val secretsJson: JsonNode?
        val valueResponse: GetSecretValueResult?
        val valueRequest: GetSecretValueRequest = GetSecretValueRequest().withSecretId(secretName)
        try {
            valueResponse = secretManager.getSecretValue(valueRequest)
        } catch (e: AWSSecretsManagerException) {
            logger.error("The requested secret $secretName was not found: ${e.message}")
            throw ApplicationException("The requested secret $secretName was not found: ${e.message}")
        }
        if (valueResponse != null) {
            try {
                secretsJson = objectMapper.readTree(valueResponse.secretString)
                return secretsJson
            } catch (e: IOException) {
                logger.error("Exception while retrieving secret values: " + e.message)
                throw ApplicationException("Exception while retrieving secret values: " + e.message)
            }
        } else {
            logger.error("The Secret String returned is null")
            throw ApplicationException("The Secret String returned is null")
        }
    }


}