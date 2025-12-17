package uz.pdp.inventorymanagementsystem.service

import uz.pdp.inventorymanagementsystem.base.BaseServiceImpl
import uz.pdp.inventorymanagementsystem.dto.TagCreateDTO
import uz.pdp.inventorymanagementsystem.dto.TagResponseDTO
import uz.pdp.inventorymanagementsystem.dto.TagUpdateDTO
import uz.pdp.inventorymanagementsystem.exception.TagNotFoundException
import uz.pdp.inventorymanagementsystem.mapper.TagMapper
import uz.pdp.inventorymanagementsystem.model.Tag
import uz.pdp.inventorymanagementsystem.repo.TagRepo
import org.springframework.stereotype.Service
import uz.pdp.inventorymanagementsystem.enums.Status
import uz.pdp.inventorymanagementsystem.exception.ValidationException
import java.time.Instant
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


    override fun create(dto: TagCreateDTO): TagResponseDTO {
        validateCreate(dto)

        val tag = convertCreateDtoToEntity(dto)

        val saved = saveAndRefresh(tag)
        return mapper.toDTO(saved)
    }

    override fun getById(id: UUID): TagResponseDTO {
        val tag = getByIdOrThrow(id) // active + inactive
        return mapper.toDTO(tag)
    }

    override fun update(id: UUID, dto: TagUpdateDTO): TagResponseDTO {
        val tag = getActive(id)
        validateUpdate(id, dto)
        updateEntityFromDto(tag, dto)
        val updated = saveAndRefresh(tag)
        return mapper.toDTO(updated)
    }

    override fun delete(id: UUID) {
        val tag = getActive(id)
        validateDelete(id) // hozircha activelarni soft delete qiladi kk bo'lsa inactivega ham logika yozsa bo'ladi
        repository.trash(tag.id!!)
    }

    /* ===================== HELPERS ===================== */

    fun getByIdOrThrow(id: UUID): Tag =
        getEntityOrNull(id) ?: throw TagNotFoundException(
            "Tag with id '$id' not found."
        )

    fun getActive(id: UUID): Tag =
        getByIdOrThrow(id).apply {
            if (status != Status.ACTIVE)
                throw ValidationException(mapOf(
                        "tag" to "Tag with id '$id' is INACTIVE."
                    )
                )
        }

    /* ===================== MAPPING ===================== */

    override fun convertCreateDtoToEntity(dto: TagCreateDTO): Tag {
        return Tag().apply {
            name = dto.name
        }
    }

    override fun updateEntityFromDto(entity: Tag, dto: TagUpdateDTO) {
        dto.name?.let { entity.name = it }
        dto.updatedAt = Instant.now()
    }

    /* ===================== VALIDATION ===================== */

    override fun validateCreate(dto: TagCreateDTO) {
        val errors = mutableMapOf<String, String>()

        dto.name.takeIf { repository.existsByNameAndDeletedFalse(it) }
            ?.let { name ->
                errors["name"] = "Tag with name '$name' already exists."
            }

        if (errors.isNotEmpty()) throw ValidationException(errors)
    }

    override fun validateUpdate(id: UUID, dto: TagUpdateDTO) {
        val current = getByIdOrThrow(id) // inactive ones can also be updated

        val errors = mutableMapOf<String, String>()
        dto.name?.let { name ->
            repository.findByNameAndDeletedFalse(name)
                ?.takeIf { it.id != current.id }
                ?.let { errors["name"] = "Tag with name '$name' already exists" }
        }

        if (errors.isNotEmpty()) throw ValidationException(errors)
    }
}