package uz.pdp.inventorymanagementsystem.dto

import uz.pdp.inventorymanagementsystem.base.BaseDTO
import java.time.Instant

data class TagResponseDTO(
    var name: String = ""
) : BaseDTO()

data class TagCreateDTO(
    var name: String
)

data class TagUpdateDTO(
    var name: String? = null,
    var updatedAt: Instant
)