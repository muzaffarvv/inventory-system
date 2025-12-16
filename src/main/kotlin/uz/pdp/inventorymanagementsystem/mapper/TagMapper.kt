package uz.pdp.inventorymanagementsystem.mapper

import org.springframework.stereotype.Component
import uz.pdp.inventorymanagementsystem.base.BaseMapper
import uz.pdp.inventorymanagementsystem.dto.TagCreateDTO
import uz.pdp.inventorymanagementsystem.dto.TagResponseDTO
import uz.pdp.inventorymanagementsystem.model.Tag

@Component
class TagMapper : BaseMapper<Tag, TagResponseDTO> {

    override fun toDTO(entity: Tag): TagResponseDTO {
        return TagResponseDTO(
            name = entity.name
        ).apply {
            mapBaseFields(entity, this)
        }
    }

    fun toEntity(dto: TagCreateDTO): Tag {
        return Tag().apply {
            name = dto.name
        }
    }

    override fun toDTOList(entities: List<Tag>): List<TagResponseDTO> {
        return entities.map { toDTO(it) }
    }
}