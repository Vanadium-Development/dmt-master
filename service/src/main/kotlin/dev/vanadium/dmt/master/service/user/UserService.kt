package dev.vanadium.dmt.master.service.user

import dev.vanadium.dmt.master.commons.authentication.UserContext
import dev.vanadium.dmt.master.domainmodel.user.User
import dev.vanadium.dmt.master.persistence.UserRepository
import dev.vanadium.dmt.master.service.user.external.KeycloakUserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class UserService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var keycloakUserService: KeycloakUserService

    fun bootstrapUser(externalId: String): User {

        val user = userRepository.findByExternalId(externalId) ?: run {
            val newUser = userRepository.save(User(externalId))

            logger.info("Created new user $newUser")

            return@run newUser
        }

        logger.info("Successful bootstrapping of user $user")

        return user
    }

    fun getUserInfo(externalId: String): UserContext.UserInfo {
        val fetchedUser = keycloakUserService.fetchUserInfo(externalId)

        return UserContext.UserInfo(fetchedUser.firstName, fetchedUser.lastName, fetchedUser.username, fetchedUser.email)
    }

    fun getById(userId: UUID): User? {
        return userRepository.findById(userId).getOrNull()
    }
}