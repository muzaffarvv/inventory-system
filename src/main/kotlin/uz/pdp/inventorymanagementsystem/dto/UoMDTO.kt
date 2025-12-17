package uz.pdp.inventorymanagementsystem.dto

import uz.pdp.inventorymanagementsystem.base.BaseDTO
import java.time.Instant

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
    var name: String? = null,
    var updatedAt: Instant
)