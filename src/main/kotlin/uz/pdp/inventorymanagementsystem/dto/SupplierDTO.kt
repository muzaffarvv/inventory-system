package uz.pdp.inventorymanagementsystem.dto

import uz.pdp.inventorymanagementsystem.base.BaseDTO
import java.util.UUID

data class SupplierResponseDTO(
    var name: String = "",
    var phone: String = "",
    var brands: Set<BrandResponseDTO> = emptySet()
) : BaseDTO()

data class SupplierCreateDTO(
    var name: String,
    var phone: String,
    var brandIds: Set<UUID> = emptySet()
)

data class SupplierUpdateDTO(
    var name: String? = null,
    var phone: String? = null,
    var brandIds: Set<UUID>? = null
)