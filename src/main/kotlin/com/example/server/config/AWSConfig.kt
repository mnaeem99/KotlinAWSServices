package com.example.server.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.secretsmanager.AWSSecretsManager
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClient
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.task.AsyncTaskExecutor
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.converter.MessageConverter
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.*

@Configuration
class AWSConfig {

    @Value("\${aws.accessKeyId}")
    private lateinit var accessKeyId:String
    @Value("\${aws.secretKey}")
    private lateinit var secretKey:String
    @Value("\${aws.s3.region}")
    private lateinit var regionS3:String
    @Value("\${aws.sns.region}")
    private lateinit var regionSNS:String
    @Value("\${aws.ses.region}")
    private lateinit var regionSES:String
    @Value("\${aws.sqs.region}")
    private lateinit var sqsRegion:String
    @Value("\${aws.secretManager.region}")
    private lateinit var secretRegion:String
    @Bean
    fun queueMessagingTemplate(): QueueMessagingTemplate {
        return QueueMessagingTemplate(amazonSQSAsync())
    }

    @Bean
    @Primary
    fun amazonSQSAsync(): AmazonSQSAsync {
        return AmazonSQSAsyncClientBuilder.standard().withRegion(sqsRegion)
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKeyId, secretKey)))
            .build()
    }

    @Bean
    @Primary
    fun simpleMessageListenerContainerFactory(amazonSQSAsync: AmazonSQSAsync?): SimpleMessageListenerContainerFactory? {
        val factory = SimpleMessageListenerContainerFactory()
        factory.setAmazonSqs(amazonSQSAsync)
        factory.setAutoStartup(true)
        factory.setMaxNumberOfMessages(10)
        factory.setTaskExecutor(createDefaultTaskExecutor())
        return factory
    }
    protected fun createDefaultTaskExecutor(): AsyncTaskExecutor? {
        val threadPoolTaskExecutor = ThreadPoolTaskExecutor()
        threadPoolTaskExecutor.setThreadNamePrefix("SQSExecutor - ")
        threadPoolTaskExecutor.corePoolSize = 100
        threadPoolTaskExecutor.maxPoolSize = 100
        threadPoolTaskExecutor.setQueueCapacity(2)
        threadPoolTaskExecutor.afterPropertiesSet()
        return threadPoolTaskExecutor
    }

    @Bean
    fun queueMessageHandlerFactory(
        mapper: ObjectMapper, amazonSQSAsync: AmazonSQSAsync?
    ): QueueMessageHandlerFactory? {
        val queueHandlerFactory = QueueMessageHandlerFactory()
        queueHandlerFactory.setAmazonSqs(amazonSQSAsync)
        queueHandlerFactory.setArgumentResolvers(
            Collections.singletonList(
                PayloadMethodArgumentResolver(jackson2MessageConverter(mapper))
            ) as List<HandlerMethodArgumentResolver>?
        )
        return queueHandlerFactory
    }

    private fun jackson2MessageConverter(mapper: ObjectMapper): MessageConverter {
        val converter = MappingJackson2MessageConverter()
        converter.objectMapper = mapper
        return converter
    }

    @Bean
    fun initializeAmazonS3(): AmazonS3 {
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKeyId, secretKey)))
            .withRegion(regionS3).build()
    }
    @Bean
    fun initializeAmazonSNS(): AmazonSNS {
        return AmazonSNSClientBuilder.standard()
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKeyId, secretKey)))
            .withRegion(regionSNS).build()
    }
    @Bean
    fun initializeAmazonSimpleEmailService(): AmazonSimpleEmailService {
        return AmazonSimpleEmailServiceClientBuilder.standard()
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKeyId, secretKey)))
            .withRegion(regionSES).build()
    }
    @Bean
    fun awsSecretsManager(): AWSSecretsManager {
        return AWSSecretsManagerClient.builder()
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKeyId, secretKey)))
            .withRegion(secretRegion)
            .build()
    }
}