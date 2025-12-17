package uz.pdp.inventorymanagementsystem.dto

import uz.pdp.inventorymanagementsystem.base.BaseDTO
import java.time.Instant

data class WarehouseResponseDTO(
    var name: String = "",
    var address: String? = null,
    var code: String = "",
    val suppliers: Set<SupplierResponseDTO>
) : BaseDTO()

data class WarehouseCreateDTO(
    var name: String,
    var address: String? = null
)

data class WarehouseUpdateDTO(
    var name: String? = null,
    var address: String? = null,
    var updatedAt: Instant
)