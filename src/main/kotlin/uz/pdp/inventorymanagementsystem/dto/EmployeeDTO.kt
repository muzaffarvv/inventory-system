package uz.pdp.inventorymanagementsystem.dto

import uz.pdp.inventorymanagementsystem.base.BaseDTO
import uz.pdp.inventorymanagementsystem.model.AuthRole
import java.util.UUID

data class EmployeeResponseDTO(
    var firstName: String = "",
    var lastName: String = "",
    var phone: String = "",
    var employeeCode: String = "",
    var warehouseResponseDTO: WarehouseResponseDTO,
    var roles: Set<AuthRoleDTO> = emptySet()
) : BaseDTO()

data class EmployeeCreateDTO(
    var firstName: String,
    var lastName: String,
    var phone: String,
    var password: String,
    var warehouseId: UUID
)

data class EmployeeUpdateDTO(
    var firstName: String? = null,
    var lastName: String? = null,
    var phone: String? = null,
    var password: String? = null,
    var warehouseId: UUID? = null,
    var roleCodes: List<String>? = null
)

data class AuthRoleDTO(
    var code: String,
    val name: String
) {
    companion object {
        fun fromEntity(role: AuthRole): AuthRoleDTO {
            return AuthRoleDTO(
                code = role.code,
                name = role.name
            )
        }
    }
}

