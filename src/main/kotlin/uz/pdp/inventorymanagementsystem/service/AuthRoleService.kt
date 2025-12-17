package uz.pdp.inventorymanagementsystem.service

import org.springframework.stereotype.Service
import uz.pdp.inventorymanagementsystem.exception.RoleNotFoundException
import uz.pdp.inventorymanagementsystem.model.AuthRole
import uz.pdp.inventorymanagementsystem.repo.AuthRoleRepo

@Service
class AuthRoleService(
    private val roleRepo: AuthRoleRepo
) {

    fun createIfNotExists(
        name: String,
        code: String,
        permissions: Set<Unit> = emptySet()
    ): AuthRole {
        require(code.isNotBlank()) { "Role code must not be blank" }

        return roleRepo.findByCode(code)
            ?: roleRepo.save(
                AuthRole(
                    name1 = "name1",
                    code1 = "code1",
                    permissions1 = mutableSetOf()
                ).apply {
                    this.name = name
                    this.code = code
                    this.permissions = permissions.toMutableSet()
                }
            )
    }

    fun getByCode(code: String): AuthRole {
        return roleRepo.findByCode(code)
            ?: throw RoleNotFoundException("Role not found: $code")
    }
}
