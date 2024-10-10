package dev.vanadium.dmt.master.service.file

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "vanadium.dmt.file.upload")
class FileUploadProperties {

    lateinit var preflightJwtSecret: String
    lateinit var preflightFileRetention: Duration
}