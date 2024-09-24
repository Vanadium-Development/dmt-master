package dev.vanadium.dmt.master.authentication.service

import dev.vanadium.dmt.master.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class AuthService {

    @Autowired
    private lateinit var userService: UserService

    fun login(externalId: String): String {

        val user = userService.bootstrapUser(externalId)

        return ""
    }
}