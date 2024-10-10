package dev.vanadium.dmt.master.service.file

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Service

@Service
class FileHousekeepingService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var taskScheduler: TaskScheduler

    @Autowired
    private lateinit var fileUploadProperties: FileUploadProperties

    @Autowired
    private lateinit var fileService: FileService


    @PostConstruct
    fun init() {
        logger.info("Initializing file housekeeping")

        taskScheduler.scheduleAtFixedRate({
            logger.info("Cleaning up stale PREFLIGHT files")

            val expiredFiles = fileService.findExpiredFiles()

            if(expiredFiles.isEmpty()) {
                logger.info("No stale files to clean up!")
                return@scheduleAtFixedRate
            }

            fileService.deleteAll(expiredFiles)

            logger.info("Cleanup complete!")
        }, fileUploadProperties.preflightFileRetention)
    }
}