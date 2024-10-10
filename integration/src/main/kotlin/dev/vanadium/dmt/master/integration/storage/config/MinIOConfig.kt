package dev.vanadium.dmt.master.integration.storage.config

import dev.vanadium.dmt.master.integration.storage.S3StorageIntegrationProperties
import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinIOConfig {

    @Autowired
    private lateinit var s3StorageIntegrationProperties: S3StorageIntegrationProperties


    @Bean
    fun minioClient(): MinioClient = MinioClient.Builder()
        .endpoint(s3StorageIntegrationProperties.host)
        .credentials(s3StorageIntegrationProperties.accessKey, s3StorageIntegrationProperties.secretKey)
        .build()

}