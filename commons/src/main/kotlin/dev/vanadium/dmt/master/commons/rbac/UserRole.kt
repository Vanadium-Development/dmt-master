package dev.vanadium.dmt.master.commons.rbac

enum class UserRole(val roleName: String, val description: String) {
    INCIDENT_MANAGER("incident_manager", "Allows the user to manage and resolve incidents.")
}