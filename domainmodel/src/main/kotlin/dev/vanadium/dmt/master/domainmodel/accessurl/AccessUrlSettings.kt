package dev.vanadium.dmt.master.domainmodel.accessurl

import java.time.Instant

data class AccessUrlSettings(
    val validFrom: Instant?,
    val validTo: Instant?,

)
