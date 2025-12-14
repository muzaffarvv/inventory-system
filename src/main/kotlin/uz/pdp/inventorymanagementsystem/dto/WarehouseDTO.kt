package uz.pdp.inventorymanagementsystem.dto

import uz.pdp.inventorymanagementsystem.base.BaseDTO

data class WarehouseResponseDTO(
    var name: String = "",
    var address: String? = null
) : BaseDTO()

data class WarehouseCreateDTO(
    var name: String,
    var address: String? = null
)

data class WarehouseUpdateDTO(
    var name: String? = null,
    var address: String? = null
)