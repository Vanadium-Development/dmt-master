package dev.vanadium.dmt.master.service.user

import dev.vanadium.dmt.master.domainmodel.User
import dev.vanadium.dmt.master.persistence.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var userRepository: UserRepository

    fun bootstrapUser(externalId: String): User {

        val user = userRepository.findByExternalId(externalId) ?: run {
            val newUser = User(externalId)

            logger.info("Created new user $newUser")

            return@run newUser
        }

        logger.info("Successful bootstrapping of user $user")

        return user
    }

}