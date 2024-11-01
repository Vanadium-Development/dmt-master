package dev.vanadium.dmt.master.integration.storage.impl

import dev.vanadium.dmt.master.commons.utils.sha256
import dev.vanadium.dmt.master.integration.storage.S3StorageIntegrationProperties
import dev.vanadium.dmt.master.integration.storage.S3StorageService
import io.minio.*
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.OutputStream

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

        if (!bucketExists) {
            logger.info("S3 Bucket '$bucket' does not exist... creating...")
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(s3StorageIntegrationProperties.bucket).build())
            logger.info("S3 Bucket successfully created.")
        } else {
            logger.info("S3 Bucket '$bucket' already exists.")
        }
    }


    // do not use because in production (look at KDocs from the interface)
    override fun createFile(data: ByteArray): String {


        val name = data.sha256()

        logger.info("Start uploading file '$name' to S3...")

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

    override fun createFile(multipartFile: MultipartFile, fileSize: Long): String {

        /**
         * Here we have to use the multipartFile.resource InputStream because the .sha256() method is used as a sink
         * and thus consumes our bytes. This is not desirable because we need the data after that to upload the file
         * to S3. Basically we're abusing the fact that MultipartFile is offering us two InputStreams for some reason
         */
        val name = multipartFile.resource.inputStream.sha256()

        logger.info("Start uploading file '$name' to S3...")

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(s3StorageIntegrationProperties.bucket)
                .stream(multipartFile.inputStream, fileSize, -1)
                .`object`(name)
                .build()
        )



        return name
    }


    override fun deleteFile(objectId: String) {
        minioClient.removeObject(
            RemoveObjectArgs.builder().bucket(s3StorageIntegrationProperties.bucket).`object`(objectId).build()
        )
        logger.info("Deleted S3 file '$objectId'")
    }

    override fun downloadFile(objectId: String): ByteArrayOutputStream {
        val stream = ByteArrayOutputStream()
        minioClient.getObject(
            GetObjectArgs(
                DownloadObjectArgs.builder().`object`(objectId)
                    .bucket(s3StorageIntegrationProperties.bucket)
                    .build()
            )
        ).transferTo(stream)

        return stream
    }


}