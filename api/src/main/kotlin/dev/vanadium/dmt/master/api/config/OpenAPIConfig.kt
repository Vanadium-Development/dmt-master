package dev.vanadium.dmt.master.api.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenAPIConfig {

    @Autowired
    private lateinit var buildProperties: BuildProperties

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("DMT Master")
                    .version(buildProperties.version)
                    .description("Master Node Backend for Distributed Media Transfer")
            ).components(
                Components()
                    .addSecuritySchemes(
                        DmtSpecificationSecurityRequirements.TOKEN, SecurityScheme()
                            .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                    )
            )

    }
}