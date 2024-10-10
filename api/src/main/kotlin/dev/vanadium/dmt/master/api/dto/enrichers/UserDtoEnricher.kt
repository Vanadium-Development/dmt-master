package dev.vanadium.dmt.master.api.dto.enrichers

import dev.vanadium.dmt.master.api.dto.EnrichedUserDto
import dev.vanadium.dmt.master.api.dto.UserDto
import dev.vanadium.dmt.master.api.dto.toDto
import dev.vanadium.dmt.master.commons.enrichment.Enricher
import dev.vanadium.dmt.master.commons.error.DmtError
import dev.vanadium.dmt.master.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserDtoEnricher : Enricher<UserDto, EnrichedUserDto> {

    @Autowired
    private lateinit var userService: UserService

    /**
     * enriches the user dto with user information and returns an instance of EnrichedUserDto
     */
    override fun enrich(input: UserDto): EnrichedUserDto {
        val user = userService.getById(input.id) ?: throw DmtError.DTO_ENRICHMENT_ERROR.toThrowableException(null, input::class.java.name, EnrichedUserDto::class.java.name, "Retrieving User from database resulted in null value.")

        val userInfo = try {
            userService.getUserInfo(user.externalId).toDto()
        } catch (e: Exception) {
            throw DmtError.DTO_ENRICHMENT_ERROR.toThrowableException(e, input::class.java.name, Enricher::class.java.name, "Retrieving User Info from Authorization backend resulted in an error")
        }


        return EnrichedUserDto(user.id, user.externalId, userInfo)
    }
}