package uz.pdp.inventorymanagementsystem.dto

import uz.pdp.inventorymanagementsystem.base.BaseDTO
import java.util.UUID

data class SupplierResponseDTO(
    var about: String = "",
    var phone: String = "",
    var brand: BrandResponseDTO?,
    val warehouse: WarehouseResponseDTO?
) : BaseDTO()

data class SupplierCreateDTO(
    var about: String,
    var phone: String,
    var brandId: UUID,
    var warehouseId: UUID
)

data class SupplierUpdateDTO(
    var about: String? = null,
    var phone: String? = null,
    var brandId: UUID? = null,
    var warehouseId: UUID? = null
)