package uz.pdp.inventorymanagementsystem.service

import uz.pdp.inventorymanagementsystem.base.BaseServiceImpl
import uz.pdp.inventorymanagementsystem.dto.TagCreateDTO
import uz.pdp.inventorymanagementsystem.dto.TagResponseDTO
import uz.pdp.inventorymanagementsystem.dto.TagUpdateDTO
import uz.pdp.inventorymanagementsystem.exception.TagAlreadyExistsException
import uz.pdp.inventorymanagementsystem.exception.TagNotFoundException
import uz.pdp.inventorymanagementsystem.mapper.TagMapper
import uz.pdp.inventorymanagementsystem.model.Tag
import uz.pdp.inventorymanagementsystem.repo.TagRepo
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TagService(
    override val repository: TagRepo,
    override val mapper: TagMapper
) : BaseServiceImpl<
        Tag,
        TagCreateDTO,
        TagUpdateDTO,
        TagResponseDTO,
        TagMapper,
        TagRepo
        >(repository, mapper) {

    override fun getById(id: UUID): TagResponseDTO {
        val tag = getTagOrThrow(id)
        return mapper.toDTO(tag)
    }

    override fun update(id: UUID, dto: TagUpdateDTO): TagResponseDTO {
        val tag = getTagOrThrow(id)
        validateUpdate(id, dto)
        updateEntityFromDto(tag, dto)
        val updated = saveAndRefresh(tag)
        return mapper.toDTO(updated)
    }

    override fun delete(id: UUID) {
        val tag = getTagOrThrow(id)
        validateDelete(id)
        repository.trash(tag.id!!)
    }

    /* ===================== HELPERS ===================== */

    private fun getTagOrThrow(id: UUID): Tag =
        getEntityOrNull(id) ?: throw TagNotFoundException(id)

    /* ===================== MAPPING ===================== */

    override fun convertCreateDtoToEntity(dto: TagCreateDTO): Tag =
        Tag().apply { name = dto.name }

    override fun updateEntityFromDto(entity: Tag, dto: TagUpdateDTO) {
        dto.name?.let { entity.name = it }
    }

    /* ===================== VALIDATION ===================== */

    override fun validateCreate(dto: TagCreateDTO) {
        if (repository.existsByNameAndDeletedFalse(dto.name))
            throw TagAlreadyExistsException("Tag with name '${dto.name}' already exists.")
    }

    override fun validateUpdate(id: UUID, dto: TagUpdateDTO) {
        dto.name?.let { name ->
            repository.findByNameAndDeletedFalse(name)?.let { existing ->
                if (existing.id != id)
                    throw TagAlreadyExistsException("Tag with name '$name' already exists.")
            }
        }
    }
}
