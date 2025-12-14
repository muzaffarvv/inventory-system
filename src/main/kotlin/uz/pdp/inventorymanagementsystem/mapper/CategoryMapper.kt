package uz.pdp.inventorymanagementsystem.mapper

import org.springframework.stereotype.Component
import uz.pdp.inventorymanagementsystem.base.BaseMapper
import uz.pdp.inventorymanagementsystem.dto.CategoryCreateDTO
import uz.pdp.inventorymanagementsystem.dto.CategoryResponseDTO
import uz.pdp.inventorymanagementsystem.model.Category

@Component
class CategoryMapper : BaseMapper<Category, CategoryResponseDTO> {

    override fun toDTO(entity: Category): CategoryResponseDTO {
        return CategoryResponseDTO(
            name = entity.name,
            parentId = entity.parent?.id,
            parentName = entity.parent?.name,
            childrenIds = entity.children.mapNotNull { it.id }
        ).apply {
            mapBaseFields(entity, this)
        }
    }

    fun toEntity(dto: CategoryCreateDTO, parent: Category?): Category {
        return Category().apply {
            name = dto.name
            this.parent = parent
        }
    }
}