package dev.vanadium.dmt.master.commons.authentication

import dev.vanadium.dmt.master.domainmodel.user.User

open class UserContext(
    open val tenant: User,
    open val userInfo: UserInfo,
    open val roles: List<String>
) {
    open class UserInfo(
        open val firstName: String,
        open val lastName: String,
        open val username: String,
        open val email: String
    )


    override fun toString(): String {
        return "${userInfo.firstName} ${userInfo.lastName} ($tenant)"
    }
}