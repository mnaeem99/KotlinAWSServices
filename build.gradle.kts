import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("jacoco")
	id("org.springframework.boot") version "2.5.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.20"
	kotlin("plugin.spring") version "1.5.20"
	kotlin("plugin.jpa") version "1.5.20"
}
group = "com.example"
version = "0.0.1-SNAPSHOT"


java.sourceCompatibility = JavaVersion.VERSION_11

tasks.getByName<Jar>("jar") {
	enabled = true
}
springBoot {
	mainClass.set("com.example.server.ServerApplicationKt")
}

repositories {
	mavenCentral()
}

dependencies {
	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// API
	implementation("org.springframework.boot:spring-boot-starter-web")

	// AWS
	implementation(platform("com.amazonaws:aws-java-sdk-bom:1.11.1000"))
	implementation("com.amazonaws:aws-java-sdk-s3")
	implementation("com.amazonaws:aws-java-sdk-sns:1.12.105")
	implementation("com.amazonaws:aws-java-sdk-secretsmanager:1.12.300")
	implementation("com.amazonaws:aws-java-sdk-ses:1.12.455")
	implementation("org.springframework.cloud:spring-cloud-aws-messaging:2.2.6.RELEASE")


	// DB
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.hibernate:hibernate-spatial:5.5.3.Final")
	implementation("com.bedatadriven:jackson-datatype-jts:2.4")
	implementation("org.liquibase:liquibase-core")
	runtimeOnly("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-starter-cache")

	// Utility
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.auth0:java-jwt:3.18.1")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.openapitools:jackson-databind-nullable:0.2.1")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("io.mockk:mockk:1.10.4")


	//lombok
	compileOnly("org.projectlombok:lombok:1.18.4")


}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
jacoco {
	toolVersion = "0.8.7"
}
