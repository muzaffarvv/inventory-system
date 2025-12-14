package uz.pdp.inventorymanagementsystem.dto

import uz.pdp.inventorymanagementsystem.base.BaseDTO
import java.util.UUID

data class CategoryResponseDTO(
    var name: String = "",
    var parentId: UUID? = null,
    var parentName: String? = null,
    var childrenIds: List<UUID> = emptyList()
) : BaseDTO()

data class CategoryCreateDTO(
    var name: String,
    var parentId: UUID? = null
)

data class CategoryUpdateDTO(
    var name: String? = null,
    var parentId: UUID? = null
)