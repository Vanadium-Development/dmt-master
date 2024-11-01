package dev.vanadium.dmt.master.service.namespace

import dev.vanadium.dmt.master.commons.authentication.UserContext
import dev.vanadium.dmt.master.commons.utils.sha256
import dev.vanadium.dmt.master.domainmodel.namespace.Namespace
import dev.vanadium.dmt.master.persistence.NamespaceRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.random.Random

@Service
class NamespaceService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var namespaceRepository: NamespaceRepository

    @Autowired
    private lateinit var userContext: UserContext

    fun createNamespace(name: String): Namespace {
        val slug = createUrlSlug()

        val namespace = namespaceRepository.save(Namespace(name, slug, userContext.tenant))

        logger.info("Namespace $namespace was created by ${userContext.tenant}")

        return namespace
    }


    // TODO: Implement with membership
    fun getNamespacesByUser(user: UUID, pageable: Pageable): Page<Namespace> {
        val namespaces = namespaceRepository.findByCreatedBy(user, pageable)

        return namespaces
    }


    private fun createUrlSlug(): String {
        val slug = Random.nextBytes(16).sha256()

        if(namespaceRepository.slugExists(slug))
            return createUrlSlug()

        return slug
    }

}