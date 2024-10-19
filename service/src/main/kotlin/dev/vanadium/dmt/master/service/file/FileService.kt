package dev.vanadium.dmt.master.service.file

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import dev.vanadium.dmt.master.commons.authentication.UserContext
import dev.vanadium.dmt.master.commons.correlation.MDCUtils
import dev.vanadium.dmt.master.commons.error.DmtError
import dev.vanadium.dmt.master.domainmodel.file.DistributedFile
import dev.vanadium.dmt.master.domainmodel.file.DistributedFileStatus
import dev.vanadium.dmt.master.integration.storage.S3StorageService
import dev.vanadium.dmt.master.persistence.DistributedFileRepository
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.jvm.optionals.getOrNull

private const val PREFLIGHT_TOKEN_ISSUER = "dmt.vanadium.dev"
private const val PREFLIGHT_TOKEN_AUDIENCE = "dmt.vanadium.dev/file/preflight"

@Service
class FileService {



    @Autowired
    private lateinit var s3StorageService: S3StorageService

    @Autowired
    private lateinit var userContext: UserContext

    @Autowired
    private lateinit var distributedFileRepository: DistributedFileRepository

    @Autowired
    private lateinit var fileUploadProperties: FileUploadProperties


    private lateinit var preflightJwtAlgorithm: Algorithm

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostConstruct
    fun init() {
        this.preflightJwtAlgorithm = Algorithm.HMAC256(fileUploadProperties.preflightJwtSecret)
    }

    fun preflightFile(name: String): Pair<String, Instant> {
        val file = store(DistributedFile(name, null, userContext.tenant))

        val expiresAt = Instant.now() + fileUploadProperties.preflightFileRetention

        val preflightToken = JWT.create()
            .withIssuer(PREFLIGHT_TOKEN_ISSUER)
            .withAudience(PREFLIGHT_TOKEN_AUDIENCE)
            .withClaim("dfid", file.dfid.toString())
            .withClaim("correlationId", MDCUtils.getCorrelationId())
            .withExpiresAt(expiresAt)
            .withIssuedAt(Instant.now())
            .withNotBefore(Instant.now())
            .sign(preflightJwtAlgorithm)

        return preflightToken to expiresAt
    }

    fun finalizeFileUpload(preflightToken: String, multipartFile: MultipartFile, fileSize: Long): DistributedFile {

        val decodedToken = try {
            JWT.require(preflightJwtAlgorithm)
                .withIssuer(PREFLIGHT_TOKEN_ISSUER)
                .withAudience(PREFLIGHT_TOKEN_AUDIENCE)
                .withClaimPresence("dfid")
                .withClaimPresence("correlationId")
                .withClaimPresence("iat")
                .build()
                .verify(preflightToken)
        } catch (e: JWTVerificationException) {
            throw DmtError.FILE_UPLOAD_ERROR.toThrowableException(e, "Preflight Token validation failed")
        }

        val correlationId = decodedToken.getClaim("correlationId").asString()
        MDCUtils += correlationId

        val dfid = decodedToken.getClaim("dfid").`as`(UUID::class.java)


        var file = getByDfid(dfid) ?: throw DmtError.FILE_UPLOAD_ERROR.toThrowableException(null, "File with DFID '$dfid' not found.")

        if(file.status != DistributedFileStatus.PREFLIGHT) {
            throw DmtError.FILE_UPLOAD_ERROR.toThrowableException(null, "An upload for the file '$dfid' is only allowed, when it's status = DistributedFileStatus.PREFLIGHT")
        }

        val objectId = s3StorageService.createFile(multipartFile, fileSize)

        try {
            file.objectId = objectId
            file.status = DistributedFileStatus.UPLOADED
            file.fileSize = fileSize


            file = store(file)

            logger.info("Distributed File $file was successfully created. (Preflight occurred at ${decodedToken.issuedAtAsInstant})")

            return file
        } catch (e: Exception) {
            handleFileUploadError(objectId, e)
        }
    }

    fun updateFile(dfid: UUID, fileName: String?) {
        val file = getAuthorizedByDfid(dfid)

        fileName?.let {
            file.name = fileName.ifEmpty { file.name }
        }

        store(file)
    }

    fun deleteFile(dfid: UUID) {
        val file = getAuthorizedByDfid(dfid)

        distributedFileRepository.delete(file)

        logger.info("User $userContext deleted file $file")
    }

    fun deleteAll(files: List<DistributedFile>) {
        logger.info("Deleted '${files.size}' Distributed Files")
        distributedFileRepository.deleteAll(files)
    }

    internal fun findExpiredFiles(): List<DistributedFile> {
        val preflightFiles =  distributedFileRepository.findPreflightFiles()

        return preflightFiles.filter { (it.createdAt + fileUploadProperties.preflightFileRetention) < Instant.now() }
    }

    private fun hasAccessToFile(dfid: UUID): Boolean {
        val file = getByDfid(dfid) ?: return false

        return file.createdBy.id == userContext.tenant.id
    }

    private fun handleFileUploadError(objectId: String, e: Exception): Nothing {
        logger.error("An exception occurred while processing a file upload. Reverting file upload...")


        if (!distributedFileRepository.existsByObjectId(objectId)) {
            s3StorageService.deleteFile(objectId)
        }
        throw e
    }



    fun getAuthorizedByDfid(dfid: UUID): DistributedFile {
        if(!hasAccessToFile(dfid)) {
            throw DmtError.INSUFFICIENT_PERMISSIONS.toThrowableException(null)
        }
        return getByDfid(dfid)!! //hasAccessToFile() already does null check on the file, which means that we are sure it exists here
    }


    fun getByUser(pageable: Pageable, user: UUID) = distributedFileRepository.findByUserUploaded(pageable, user)


    private fun getByDfid(dfid: UUID) = distributedFileRepository.findById(dfid).getOrNull()


    fun store(distributedFile: DistributedFile): DistributedFile {
        if(distributedFile.objectId == null && distributedFile.status != DistributedFileStatus.PREFLIGHT) {
            throw DmtError.INTERNAL_VALIDATION_ERROR.toThrowableException(null, "A DistributedFile can only be stored with a null-valued objectId when status = DistributedFileStatus.PREFLIGHT")
        }

        if(distributedFile.status == DistributedFileStatus.PREFLIGHT && distributedFile.objectId != null) {
            throw DmtError.INTERNAL_VALIDATION_ERROR.toThrowableException(null, "A DistributedFile can only be stored with status DistributedFileStatus.PREFLIGHT when objectId is null")
        }

        val file = distributedFileRepository.save(distributedFile)

        logger.info("Stored DistributedFile: $file")
        return file
    }
}