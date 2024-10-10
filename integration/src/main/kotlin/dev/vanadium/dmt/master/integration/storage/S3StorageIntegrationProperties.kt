package dev.vanadium.dmt.master.integration.storage

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "vanadium.dmt.integration.storage.s3")
class S3StorageIntegrationProperties {
    lateinit var host: String
    lateinit var accessKey: String
    lateinit var secretKey: String
    lateinit var bucket: String
}