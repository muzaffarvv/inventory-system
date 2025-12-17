package uz.pdp.inventorymanagementsystem.utils

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.AnonymousAuthenticationToken
import uz.pdp.inventorymanagementsystem.config.CustomUserDetails
import java.util.*

object CurrentUser {

    fun get(): CustomUserDetails {
        val context = SecurityContextHolder.getContext()
        val auth = context.authentication

        if (auth == null || !auth.isAuthenticated || auth is AnonymousAuthenticationToken) {
            throw IllegalStateException("No authenticated user found")
        }

        return auth.principal as CustomUserDetails
    }

    fun id(): UUID = get().id

    fun warehouseId(): UUID = get().warehouseId

    fun hasRole(role: String): Boolean =
        get().authorities.any { it.authority == "ROLE_$role" }

    fun hasPermission(permission: String): Boolean =
        get().authorities.any { it.authority == permission }
}
