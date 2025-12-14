package uz.pdp.inventorymanagementsystem.dto

import uz.pdp.inventorymanagementsystem.base.BaseDTO

data class CurrencyResponseDTO(
    var code: String = "",
    var name: String = ""
) : BaseDTO()

data class CurrencyCreateDTO(
    var code: String,
    var name: String
)

data class CurrencyUpdateDTO(
    var code: String? = null,
    var name: String? = null
)