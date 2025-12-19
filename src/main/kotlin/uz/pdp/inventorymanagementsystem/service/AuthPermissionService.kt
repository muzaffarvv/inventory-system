package uz.pdp.inventorymanagementsystem.service

import org.springframework.stereotype.Service
import uz.pdp.inventorymanagementsystem.enums.Status
import uz.pdp.inventorymanagementsystem.model.AuthPermission
import uz.pdp.inventorymanagementsystem.repo.AuthPermissionRepo

@Service
class AuthPermissionService(
    private val repository: AuthPermissionRepo
) {

    fun createIfNotExists(name: String, code: String?): AuthPermission {
        val finalCode = code?.takeIf { it.isNotBlank() }
            ?: name.uppercase().replace(" ", "_")

        return repository.findByCode(finalCode)
            ?: repository.save(
                AuthPermission().apply {
                    this.name = name
                    this.code = finalCode
                    this.status = Status.ACTIVE
                    this.deleted = false
                }
            )
    }
}



