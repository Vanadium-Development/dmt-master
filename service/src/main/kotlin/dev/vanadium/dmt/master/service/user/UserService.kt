package dev.vanadium.dmt.master.service.user

import dev.vanadium.dmt.master.commons.authentication.UserContext
import dev.vanadium.dmt.master.domainmodel.user.User
import dev.vanadium.dmt.master.persistence.UserRepository
import dev.vanadium.dmt.master.service.user.external.KeycloakUserService
import jakarta.ws.rs.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.annotation.Scheduled
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


    @Scheduled(fixedDelayString = "PT5M")
    fun autoSynchronizeUsers() {
        userRepository.findAll().mapNotNull {
            synchronizeUser(it.id, false)
        }.chunked(50).forEach {
            userRepository.saveAll(it)
        }
    }

    fun bootstrapUser(externalId: String): User {
        val user = userRepository.findByExternalId(externalId) ?: run {
            val newUser = userRepository.save(User(externalId))

            logger.info("Created new user $newUser")

            return@run newUser
        }

        if(listOf(user.firstName, user.lastName, user.username).any { it == null }) {
            synchronizeUser(user.id, true)
        }

        logger.info("Successful bootstrapping of user $user")

        return user
    }

    fun queryUsersForSuggestion(username: String): List<User> {
        return userRepository.queryByUsername(username.lowercase(), PageRequest.of(0, 5)).content
    }

    fun getUserInfo(externalId: String): UserContext.UserInfo {
        val fetchedUser = keycloakUserService.fetchUserInfo(externalId)

        return UserContext.UserInfo(fetchedUser.firstName, fetchedUser.lastName, fetchedUser.username, fetchedUser.email)
    }

    fun getById(userId: UUID): User? {
        return userRepository.findById(userId).getOrNull()
    }

    fun store(user: User): User {
        logger.info("Storing user $user")
        return userRepository.save(user)
    }

    fun synchronizeUser(userId: UUID, storeUser: Boolean): User? {
        val user = getById(userId) ?: throw NullPointerException("Failed to synchronize user $userId: tried to synchronize user information for user, but user does not exist!")

        val userInfo = try {
            getUserInfo(user.externalId)
        } catch (e: NotFoundException) {
            logger.warn("User $user was not found in external provider, skipping synchronization!")
            return null
        }

        user.username = userInfo.username
        user.lastName = userInfo.lastName
        user.firstName = userInfo.firstName

        logger.info("Synchronizing user $user with up-to-date user information from provider")

        if(!storeUser) {
            return user
        }

        return store(user)
    }
}