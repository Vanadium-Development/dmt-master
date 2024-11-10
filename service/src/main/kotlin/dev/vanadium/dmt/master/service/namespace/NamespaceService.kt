package dev.vanadium.dmt.master.service.namespace

import dev.vanadium.dmt.master.commons.authentication.UserContext
import dev.vanadium.dmt.master.commons.error.DmtError
import dev.vanadium.dmt.master.commons.utils.sha256
import dev.vanadium.dmt.master.domainmodel.namespace.Namespace
import dev.vanadium.dmt.master.domainmodel.namespace.NamespaceUser
import dev.vanadium.dmt.master.domainmodel.namespace.NamespaceUserId
import dev.vanadium.dmt.master.domainmodel.user.User
import dev.vanadium.dmt.master.persistence.NamespaceRepository
import dev.vanadium.dmt.master.persistence.NamespaceUserRepository
import dev.vanadium.dmt.master.service.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrNull
import kotlin.random.Random

@Service
class NamespaceService {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var namespaceUserRepository: NamespaceUserRepository
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var namespaceRepository: NamespaceRepository

    @Autowired
    private lateinit var userContext: UserContext

    fun createNamespace(name: String): Namespace {
        val slug = createUrlSlug()


        val namespace = namespaceRepository.save(Namespace(name, slug, userContext.tenant))

        logger.info("Namespace $namespace was created by ${userContext.tenant}")

        addMemberInsecure(namespace.id, userContext.tenant.id, null)

        return namespace
    }


    fun addMember(namespaceId: UUID, user: UUID): NamespaceUser {
        val namespace = getAuthorizedNamespace(namespaceId)

        return addMemberInsecure(namespace.id, user, userContext.tenant.id)
    }

    @Transactional
    fun removeMember(namespaceId: UUID, user: UUID) {
        val namespace = getAuthorizedNamespace(namespaceId)

        if(namespace.createdBy.id == user) {
            throw DmtError.NAMESPACE_MEMBER_REMOVAL_NOT_POSSIBLE.toThrowableException(null, "Cannot remove creator of the namespace!")
        }

        namespaceUserRepository.deleteByUserAndNamespace(user, namespaceId)
    }

    fun queryMemberSuggestion(namespaceId: UUID, usernameQuery: String): List<User> {
        val namespace = getAuthorizedNamespace(namespaceId)
        return userService.queryUsersForSuggestion(usernameQuery).filterNot { namespace.isMember(it.id) }
    }

    private fun addMemberInsecure(namespaceId: UUID, userId: UUID, createdBy: UUID?): NamespaceUser {
        val namespace = namespaceRepository.findById(namespaceId).getOrNull()
            ?: throw DmtError.NAMESPACE_NOT_FOUND.toThrowableException(null, namespaceId)

        val user = userService.getById(userId) ?: throw DmtError.USER_NOT_FOUND.toThrowableException(null, userId)

        val createdByUser = createdBy?.let { userService.getById(createdBy) }

        val membership = NamespaceUser(NamespaceUserId(user, namespace), createdByUser)

        logger.info("User $user was made member of $namespace by $createdByUser")

        return namespaceUserRepository.save(membership)
    }


    fun getAuthorizedNamespace(namespaceId: UUID): Namespace {
        val namespace = namespaceRepository.findById(namespaceId).getOrNull()
            ?: throw DmtError.INSUFFICIENT_PERMISSIONS.toThrowableException(null)

        if (!namespace.isMember(userContext.tenant.id)) {
            throw DmtError.INSUFFICIENT_PERMISSIONS.toThrowableException(null)
        }

        return namespace
    }

    fun getNamespacesByUser(user: UUID, pageable: Pageable): Page<Namespace> {
        val namespaces = namespaceRepository.findByMember(user, pageable)

        return namespaces
    }

    fun getMembers(namespace: UUID, pageable: Pageable): Page<NamespaceUser> {
        val ns = getAuthorizedNamespace(namespace)

        return namespaceUserRepository.getMembers(ns.id, pageable)
    }


    private fun createUrlSlug(): String {
        val slug = Random.nextBytes(16).sha256()

        if (namespaceRepository.slugExists(slug))
            return createUrlSlug()

        return slug
    }


    private fun Namespace.isMember(user: UUID): Boolean {
        return namespaceUserRepository.existsByUserAndNamespace(user, this.id)
    }
}