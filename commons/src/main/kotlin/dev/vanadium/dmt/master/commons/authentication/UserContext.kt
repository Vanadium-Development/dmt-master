package dev.vanadium.dmt.master.commons.authentication

import dev.vanadium.dmt.master.domainmodel.User

data class UserContext(
    val tenant: User,
    val roles: List<String>
)