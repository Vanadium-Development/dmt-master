package dev.vanadium.dmt.master

import dev.vanadium.dmt.master.authentication.properties.AuthenticationProperties
import dev.vanadium.dmt.master.integration.keycloak.KeycloakClientProperties
import dev.vanadium.dmt.master.integration.storage.S3StorageIntegrationProperties
import dev.vanadium.dmt.master.service.file.FileUploadProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableConfigurationProperties(
    value = [
        AuthenticationProperties::class,
        KeycloakClientProperties::class,
        S3StorageIntegrationProperties::class,
        FileUploadProperties::class
    ]
)
@EnableScheduling
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
