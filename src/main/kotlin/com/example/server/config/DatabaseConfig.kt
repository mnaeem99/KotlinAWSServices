package com.example.server.config

import com.example.server.service.aws.AWSService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@Configuration
class DatabaseConfig @Autowired constructor(
    private val awsService: AWSService,
    @Value("\${aws.secrets.db}")
    private var secretsDB:String,
) {

    @Bean
    fun dataSource(): DataSource? {
        val secretsJson = awsService.getValue(secretsDB)
        if (secretsJson!=null) {
            val host: String = secretsJson.get("host").asText()
            val port: String = secretsJson.get("port").asText()
            val dbname: String = secretsJson.get("dbName").asText()
            val username: String = secretsJson.get("username").asText()
            val password: String = secretsJson.get("password").asText()
            return DataSourceBuilder
                .create()
                .url("jdbc:postgresql://$host:$port/$dbname")
                .username(username)
                .password(password)
                .build()
        }
        return null
    }
}