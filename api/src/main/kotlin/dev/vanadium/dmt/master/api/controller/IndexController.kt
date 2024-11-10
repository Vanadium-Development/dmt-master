package dev.vanadium.dmt.master.api.controller

import dev.vanadium.dmt.master.api.config.DmtSpecificationTags
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.info.BuildProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/")
@Tag(name = DmtSpecificationTags.INDEX)
class IndexController {

    @Autowired
    private lateinit var buildProperties: BuildProperties

    @GetMapping
    fun getServiceInformation(): ServiceInfo {
        return ServiceInfo("DMT-Master", buildProperties.version, buildProperties.time)
    }

    data class ServiceInfo(
        val name: String,
        val version: String,
        val buildTime: Instant
    )
}