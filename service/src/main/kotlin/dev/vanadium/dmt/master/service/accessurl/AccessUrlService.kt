package dev.vanadium.dmt.master.service.accessurl

import dev.vanadium.dmt.master.domainmodel.accessurl.AccessUrlSettings
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AccessUrlService {

    fun createAccessUrl(slug: String, namespace: UUID, settings: AccessUrlSettings, file: UUID) {

    }
}