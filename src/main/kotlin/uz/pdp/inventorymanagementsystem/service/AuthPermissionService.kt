package uz.pdp.inventorymanagementsystem.service

import org.springframework.stereotype.Service
import uz.pdp.inventorymanagementsystem.enums.Status
import uz.pdp.inventorymanagementsystem.model.AuthPermission
import uz.pdp.inventorymanagementsystem.repo.AuthPermissionRepo

@Service
class AuthPermissionService(
    private val repository: AuthPermissionRepo
) {

    fun createIfNotExists(name: String, code: String?) {
        val finalCode = code?.takeIf { it.isNotBlank() } ?: generateCode(name)

        // Agar shu code bo'yicha permission mavjud bo'lsa, qaytadi
        if (repository.existsByCode(finalCode)) return

        val permission = AuthPermission(
            code = "ADMIN_PERMISSION",
            name = "Admin Permission"
        ).apply {
            this.name = name
            this.code = finalCode
            this.status = Status.ACTIVE
            this.deleted = false
        }

        repository.save(permission)
    }

    /**
     * Code generator (masalan, name'ni uppercase qilib ishlatish)
     */
    private fun generateCode(name: String): String {
        return name.uppercase().replace(" ", "_")
    }
}


