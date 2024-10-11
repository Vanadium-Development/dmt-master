package dev.vanadium.dmt.master.integration.storage

import org.springframework.web.multipart.MultipartFile

interface S3StorageService {
    /**
     * Usage of this method is **not** recommended! This method expects a ByteArray which is capped to 2 gigabyte.
     * This please use the createFile(MultipartFile, Long) method for production use
     */
    fun createFile(data: ByteArray): String
    fun createFile(multipartFile: MultipartFile, fileSize: Long): String
    fun deleteFile(name: String)
}