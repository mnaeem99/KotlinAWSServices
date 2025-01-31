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
import com.amazonaws.services.sns.model.AmazonSNSException
import com.amazonaws.services.sns.model.PublishRequest
import com.amazonaws.services.sns.model.PublishResult
import com.example.server.common.Constant
import com.example.server.mvc.error.ApplicationException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URL

internal class AWSServiceTest{
    private val s3:AmazonS3 = Mockito.mock(AmazonS3::class.java)
    private val sns:AmazonSNS = Mockito.mock(AmazonSNS::class.java)
    private val ses = Mockito.mock(AmazonSimpleEmailService::class.java)
    private val secretsManager: AWSSecretsManager = Mockito.mock(AWSSecretsManager::class.java)
    private val awsService = AWSService(s3, sns,ses, Constant.EMAIL, "demo-dev",secretsManager,"test")

    @Test
    fun `should given image url, when save picture, then return url`() {
        //given
        val url = URL("https://demo-storage.s3.amazonaws.com/gum")
        val bytes = byteArrayOf(0xA1.toByte(), 0x2E.toByte(), 0x38.toByte(), 0xD4.toByte(), 0x89.toByte(), 0xC3.toByte())
        val metadata = ObjectMetadata()
        metadata.contentType = "image/png"
        val putObjectRequest = PutObjectRequest("demo-dev", "gum", ByteArrayInputStream(bytes), metadata)
        val putObjectResult = Mockito.mock(PutObjectResult::class.java)
        Mockito.`when`( s3.putObject(putObjectRequest)).thenReturn(putObjectResult)
        Mockito.`when`(s3.getUrl("demo-dev","gum")).thenReturn(url)
        //when
        val result = awsService.savePicture(bytes,"gum","image/png")
        //Then
        Mockito.verify(s3).getUrl("demo-dev","gum")
        assertThat(result).isNotNull
        assertThat(result.toString()).isEqualTo(url.toString())
    }
    @Test
    fun `should given image file name, when save picture, then return exception`() {
        //given
        val bytes = byteArrayOf(0xA1.toByte(), 0x2E.toByte(), 0x38.toByte(), 0xD4.toByte(), 0x89.toByte(), 0xC3.toByte())
        val metadata = ObjectMetadata()
        metadata.contentType = "image/png"
        val amazonS3Exception = AmazonS3Exception("error")
        Mockito.doThrow(amazonS3Exception).`when`(s3).getUrl("demo-dev","gum")
        //when
        val result: Exception = Assertions.assertThrows(ApplicationException::class.java) {
            awsService.savePicture(bytes,"gum","image/png")
        }
        //Then
        assertThat(result).isNotNull
    }
    @Test
    fun `should given email info, when send email, then mock sent`() {
        //given
        val request = SendEmailRequest()
            .withDestination(
                Destination().withToAddresses("m.naeem9073@gmail.com")
            )
            .withMessage(
                Message()
                    .withBody(
                        Body()
                            .withHtml(
                                Content()
                                    .withCharset("UTF-8").withData(Constant.EMAIL_TEMPLATE)
                            )
                    )
                    .withSubject(
                        Content()
                            .withCharset("UTF-8").withData("JUnit")
                    )
            )
            .withSource(Constant.EMAIL)
        val sendEmailResult = SendEmailResult()
        sendEmailResult.messageId = "123"
        Mockito.`when`(ses.sendEmail(request)).thenReturn(sendEmailResult)
        //when
        awsService.sendEmail("m.naeem9073@gmail.com",Constant.EMAIL_TEMPLATE,"JUnit")
        //Then
        Mockito.verify(ses).sendEmail(request)
    }
    @Test
    fun `should given email info, when send email, then exception`() {
        //given
        val simpleEmailServiceException = AmazonSimpleEmailServiceException("error")
        val request = SendEmailRequest()
            .withDestination(
                Destination().withToAddresses("m.naeem9073@gmail.com")
            )
            .withMessage(
                Message()
                    .withBody(
                        Body()
                            .withHtml(
                                Content()
                                    .withCharset("UTF-8").withData(Constant.EMAIL_TEMPLATE)
                            )
                    )
                    .withSubject(
                        Content()
                            .withCharset("UTF-8").withData("JUnit")
                    )
            )
            .withSource(Constant.EMAIL)
        Mockito.doThrow(simpleEmailServiceException).`when`(ses).sendEmail(request)
        //when
        val result: Exception = Assertions.assertThrows(ApplicationException::class.java) {
            awsService.sendEmail("m.naeem9073@gmail.com", Constant.EMAIL_TEMPLATE, "JUnit")
        }
        //Then
        Mockito.verify(ses).sendEmail(request)
        assertThat(result).isNotNull
    }
    @Test
    fun `should given phone , when send sms, then return mock sent`() {
        //given
        val publishResult = PublishResult()
        publishResult.messageId = "dc420aee-1588-5b5e-a96d-51025ac641a9"
        Mockito.`when`(sns.publish(PublishRequest().withMessage("hi this is unit test").withPhoneNumber("+923086999073"))).thenReturn(publishResult)
        //when
        awsService.sendSms("+923086999073","hi this is unit test")
        //Then
        Mockito.verify(sns).publish(PublishRequest().withMessage("hi this is unit test").withPhoneNumber("+923086999073"))
    }
    @Test
    fun `should given phone , when send sms, then exception`() {
        //given
        val amazonSNSException = AmazonSNSException("error")
        Mockito.doThrow(amazonSNSException).`when`(sns).publish(PublishRequest().withMessage("hi this is unit test").withPhoneNumber("+923086999073"))
        //when
        val result: Exception = Assertions.assertThrows(ApplicationException::class.java) {
            awsService.sendSms("+923086999073", "hi this is unit test")
        }
        //Then
        Mockito.verify(sns).publish(PublishRequest().withMessage("hi this is unit test").withPhoneNumber("+923086999073"))
        assertThat(result).isNotNull
    }

    @Test
    fun `should given fileName, when getLocalization, then return inputStream`() {
        //given
        val s3Object = S3Object()
        val inputStream: InputStream = ByteArrayInputStream(Constant.INTERNATIONALIZATION.toByteArray())
        val s3ObjectInputStream = S3ObjectInputStream(inputStream, null)
        s3Object.objectContent = s3ObjectInputStream
        Mockito.`when`(s3.getObject("demo-dev", "test")).thenReturn(s3Object)
        //when
        val result = awsService.getLocalization("US")
        //Then
        Mockito.verify(s3).getObject("demo-dev","test")
        assertThat(result).isNotNull()
    }
    @Test
    fun `should given fileName, when getLocalization, then return exception`() {
        //given
        val amazonS3Exception = AmazonS3Exception("error")
        Mockito.doThrow(amazonS3Exception).`when`(s3).getObject("demo-dev", "test")
        //when
        val result: Exception = Assertions.assertThrows(ApplicationException::class.java) {
            awsService.getLocalization("test")
        }
        //Then
        Mockito.verify(s3).getObject("demo-dev","test")
        assertThat(result).isNotNull
    }

    @Test
    fun `should given file name, when getLocalization, then return json node`() {
        //given
        val s3Object = S3Object()
        val inputStream: InputStream = ByteArrayInputStream(Constant.INTERNATIONALIZATION.toByteArray())
        val s3ObjectInputStream = S3ObjectInputStream(inputStream, null)
        s3Object.objectContent = s3ObjectInputStream
        Mockito.`when`(s3.getObject("demo-dev", "test")).thenReturn(s3Object)
        //when
        val result = awsService.getLocalization("US")
        //Then
        Mockito.verify(s3).getObject("demo-dev","test")
        assertThat(result).isNotNull()
    }

    @Test
    fun `should given invalid content, when getLocalization, then return exception`() {
        //given
        val s3Object = S3Object()
        val inputStream: InputStream = ByteArrayInputStream("invalid json".toByteArray())
        val s3ObjectInputStream = S3ObjectInputStream(inputStream, null)
        s3Object.objectContent = s3ObjectInputStream
        Mockito.`when`(s3.getObject("demo-dev", "test")).thenReturn(s3Object)
        //when
        val result: Exception = Assertions.assertThrows(ApplicationException::class.java) {
            awsService.getLocalization("US")
        }
        //Then
        Mockito.verify(s3).getObject("demo-dev","test")
        assertThat(result).isNotNull
        assertThat(result.message).isEqualTo("Exception while fetching internationalization")
    }

    @Test
    fun `should given secret name, when getValue, then secret value response`() {
        //given
        val valueRequest: GetSecretValueRequest = GetSecretValueRequest().withSecretId("test")
        val valueResponse = GetSecretValueResult()
        valueResponse.secretString = Constant.SECRET_RESPONSE_GOOGLE_MAPS
        Mockito.`when`(secretsManager.getSecretValue(valueRequest)).thenReturn(valueResponse)
        //when
        val result = awsService.getValue("test")
        //Then
        Mockito.verify(secretsManager).getSecretValue(valueRequest)
        assertThat(result).isNotNull
        assertThat(result.toString()).isEqualTo(Constant.SECRET_RESPONSE_GOOGLE_MAPS)
    }

    @Test
    fun `should given invalid secret name, when getValue, then return exception`() {
        //given
        val valueRequest: GetSecretValueRequest = GetSecretValueRequest().withSecretId("test")
        val awsSecretsManagerException = AWSSecretsManagerException("error")
        Mockito.doThrow(awsSecretsManagerException).`when`(secretsManager).getSecretValue(valueRequest)
        //when
        val result: Exception = Assertions.assertThrows(ApplicationException::class.java) {
            awsService.getValue("test")
        }
        //Then
        Mockito.verify(secretsManager).getSecretValue(valueRequest)
        assertThat(result).isNotNull
        assertThat(result.message).isEqualTo("The requested secret test was not found: ${awsSecretsManagerException.message}")
    }

    @Test
    fun `should given secret name and invalid secret string, when getValue, then return exception`() {
        //given
        val valueRequest: GetSecretValueRequest = GetSecretValueRequest().withSecretId("test")
        val valueResponse = GetSecretValueResult()
        valueResponse.secretString = "}{"
        Mockito.`when`(secretsManager.getSecretValue(valueRequest)).thenReturn(valueResponse)
        //when
        val result: Exception = Assertions.assertThrows(ApplicationException::class.java) {
            awsService.getValue("test")
        }
        //Then
        Mockito.verify(secretsManager).getSecretValue(valueRequest)
        assertThat(result).isNotNull
        assertThat(result.message).contains("Exception while retrieving secret values")
    }
    @Test
    fun `should given secret name and null secret response, when getValue, then return exception`() {
        //given
        val valueRequest: GetSecretValueRequest = GetSecretValueRequest().withSecretId("test")
        Mockito.`when`(secretsManager.getSecretValue(valueRequest)).thenReturn(null)
        //when
        val result: Exception = Assertions.assertThrows(ApplicationException::class.java) {
            awsService.getValue("test")
        }
        //Then
        Mockito.verify(secretsManager).getSecretValue(valueRequest)
        assertThat(result).isNotNull
        assertThat(result.message).contains("The Secret String returned is null")
    }


}