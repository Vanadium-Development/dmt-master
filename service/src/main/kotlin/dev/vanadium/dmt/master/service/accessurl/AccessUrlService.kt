package dev.vanadium.dmt.master.service.accessurl

import dev.vanadium.dmt.master.commons.authentication.UserContext
import dev.vanadium.dmt.master.commons.context.NamespaceContext
import dev.vanadium.dmt.master.commons.error.DmtError
import dev.vanadium.dmt.master.domainmodel.accessurl.AccessUrl
import dev.vanadium.dmt.master.domainmodel.accessurl.AccessUrlSettings
import dev.vanadium.dmt.master.persistence.AccessUrlRepository
import dev.vanadium.dmt.master.service.file.FileService
import dev.vanadium.dmt.master.service.namespace.NamespaceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AccessUrlService {

    @Autowired
    private lateinit var namespaceContext: NamespaceContext

    @Autowired
    private lateinit var fileService: FileService

    @Autowired
    private lateinit var namespaceService: NamespaceService

    @Autowired
    private lateinit var accessUrlRepository: AccessUrlRepository

    @Autowired
    private lateinit var userContext: UserContext

    fun createAccessUrl(slug: String, settings: AccessUrlSettings, file: UUID): AccessUrl {
        val namespace = namespaceContext()


        val (slugAvailable, reason) = isSlugAvailable(namespace.id, slug)

        if(!slugAvailable) {
            throw DmtError.SLUG_NOT_AVAILABLE.toThrowableException(null, reason ?: "Unknown reason!")
        }

        val accessUrl = accessUrlRepository.save(AccessUrl(slug, namespace, userContext.tenant, settings, fileService.getAuthorizedByDfid(file)))

        return accessUrl
    }

    fun getAccessUrls(pageable: Pageable): Page<AccessUrl> {
        return accessUrlRepository.findByNamespace(namespaceContext().id, pageable)
    }


    private fun isSlugAvailable(namespace: UUID, slug: String): Pair<Boolean, String?> {
        val slugExists = accessUrlRepository.doesSlugExist(namespace, slug)

        return !slugExists to if(slugExists) "Slug $slug is already used in namespace!" else null
    }
}