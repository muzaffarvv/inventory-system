package uz.pdp.inventorymanagementsystem.service

import org.springframework.stereotype.Service
import uz.pdp.inventorymanagementsystem.exception.RoleNotFoundException
import uz.pdp.inventorymanagementsystem.model.AuthPermission
import uz.pdp.inventorymanagementsystem.model.AuthRole
import uz.pdp.inventorymanagementsystem.repo.AuthRoleRepo

@Service
class AuthRoleService(
    private val roleRepo: AuthRoleRepo
) {

    fun createIfNotExists(
        name: String,
        code: String,
        permissions: Set<AuthPermission> = emptySet()
    ): AuthRole {

        return roleRepo.findByCodeAndDeletedFalse(code)
            ?: roleRepo.save(
                AuthRole().apply {
                    this.name = name
                    this.code = code
                    this.permissions.addAll(permissions)
                }
            )
    }

    fun getByCode(code: String): AuthRole {
        return roleRepo.findByCodeAndDeletedFalse(code)
            ?: throw RoleNotFoundException("Role not found with code: $code")
    }

}

