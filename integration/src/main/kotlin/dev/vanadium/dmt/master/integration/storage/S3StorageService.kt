package dev.vanadium.dmt.master.integration.storage

interface S3StorageService {
    fun createFile(data: ByteArray): String
    fun deleteFile(name: String)
}