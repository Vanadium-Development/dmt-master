package dev.vanadium.dmt.master.integration.storage.impl

import dev.vanadium.dmt.master.commons.utils.sha256
import dev.vanadium.dmt.master.integration.storage.S3StorageIntegrationProperties
import dev.vanadium.dmt.master.integration.storage.S3StorageService
import io.minio.*
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream

@Service
class MinioStorageService : S3StorageService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var s3StorageIntegrationProperties: S3StorageIntegrationProperties

    @Autowired
    private lateinit var minioClient: MinioClient

    @PostConstruct
    fun setupBucket() {
        val bucket = s3StorageIntegrationProperties.bucket
        val bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())

        if(!bucketExists) {
            logger.info("S3 Bucket '$bucket' does not exist... creating...")
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(s3StorageIntegrationProperties.bucket).build())
            logger.info("S3 Bucket successfully created.")
        } else {
            logger.info("S3 Bucket '$bucket' already exists.")
        }
    }


    override fun createFile(data: ByteArray): String {


        val name = data.sha256()

        logger.info("Start uploading file '$name' to S3...")

        // TODO: Maybe we should check whether this file already exist?
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(s3StorageIntegrationProperties.bucket)
                .stream(ByteArrayInputStream(data), data.size.toLong(), -1)
                .`object`(name)
                .build()
        )

        logger.info("Successfully created S3 file '$name'")

        return name
    }

    override fun deleteFile(name: String) {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(s3StorageIntegrationProperties.bucket).`object`(name).build())
        logger.info("Deleted S3 file '$name'")
    }

}