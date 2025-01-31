# AWS Service Demo - Kotlin Spring Boot

This project is a Kotlin Spring Boot service that integrates with various AWS services, including:
- **Amazon S3** for file storage
- **Amazon SNS** for sending SMS messages
- **Amazon SES** for sending emails
- **AWS Secrets Manager** for managing application secrets

## Features

### 1. Save Picture to S3
Uploads a file to an S3 bucket and returns its public URI.

### 2. Retrieve an Object from S3
Fetches a file from S3 and returns its content as an InputStream.

### 3. Send an Email via Amazon SES
Sends an email using AWS Simple Email Service (SES).

### 4. Send an SMS via Amazon SNS
Sends an SMS message using AWS Simple Notification Service (SNS).

### 5. Retrieve Secret Values from AWS Secrets Manager
Fetches and parses JSON-formatted secrets from AWS Secrets Manager.

### 6. Localization Fetching from S3
Retrieves language-specific content stored in an S3 file.

## Technologies Used
- **Kotlin**
- **Spring Boot**
- **AWS SDK**
- **Spring Cache**
- **Spring Scheduling**

## Prerequisites
- AWS credentials configured with appropriate permissions.
- An S3 bucket.
- An AWS SNS topic for sending SMS.
- AWS SES set up for sending emails.
- Secrets stored in AWS Secrets Manager.

## Configuration
Set the following properties in `application.yml` or `application.properties`:
```properties
aws.s3.bucket=<your-bucket-name>
aws.ses.email=<your-verified-ses-email>
aws.s3.website.localization=<your-localization-file-name>
```
Ensure that the necessary AWS credentials are available in `~/.aws/credentials` or as environment variables.

## Usage

### 1. Saving a Picture
```kotlin
val content: ByteArray = File("sample.jpg").readBytes()
val uri: URI? = awsService.savePicture(content, "image.jpg", "image/jpeg")
println("File uploaded to: $uri")
```

### 2. Sending an Email
```kotlin
awsService.sendEmail("recipient@example.com", "Hello, this is a test email!", "Test Email")
```

### 3. Sending an SMS
```kotlin
awsService.sendSms("+1234567890", "This is a test SMS message.")
```

### 4. Retrieving a Secret Value
```kotlin
val secret: JsonNode? = awsService.getValue("my-secret-key")
println(secret?.toString())
```

## Running the Application
Run the Spring Boot application:
```sh
./gradlew bootRun
```
Or if using Maven:
```sh
mvn spring-boot:run
```

## License
This project is licensed under the MIT License.

