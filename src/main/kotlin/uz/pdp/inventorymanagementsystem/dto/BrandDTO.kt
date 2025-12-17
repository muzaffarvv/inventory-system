package uz.pdp.inventorymanagementsystem.dto

import uz.pdp.inventorymanagementsystem.base.BaseDTO

data class BrandResponseDTO(
    var name: String = "",
    var country: String = "",
    val suppliers: Set<SupplierResponseDTO>
) : BaseDTO()

data class BrandCreateDTO(
    var name: String,
    var country: String
)

data class BrandUpdateDTO(
    var name: String? = null,
    var country: String? = null
)