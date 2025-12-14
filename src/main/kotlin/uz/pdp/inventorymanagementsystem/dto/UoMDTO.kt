package uz.pdp.inventorymanagementsystem.dto

import uz.pdp.inventorymanagementsystem.base.BaseDTO

data class UoMResponseDTO(
    var code: String = "",
    var name: String = ""
) : BaseDTO()

data class UoMCreateDTO(
    var code: String,
    var name: String
)

data class UoMUpdateDTO(
    var code: String? = null,
    var name: String? = null
)