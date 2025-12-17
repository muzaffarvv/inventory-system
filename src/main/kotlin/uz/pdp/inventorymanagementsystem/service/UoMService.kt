package uz.pdp.inventorymanagementsystem.service

import org.springframework.stereotype.Service
import uz.pdp.inventorymanagementsystem.base.BaseServiceImpl
import uz.pdp.inventorymanagementsystem.dto.UoMCreateDTO
import uz.pdp.inventorymanagementsystem.dto.UoMResponseDTO
import uz.pdp.inventorymanagementsystem.dto.UoMUpdateDTO
import uz.pdp.inventorymanagementsystem.enums.Status
import uz.pdp.inventorymanagementsystem.exception.UoMNotFoundException
import uz.pdp.inventorymanagementsystem.exception.ValidationException
import uz.pdp.inventorymanagementsystem.mapper.UoMMapper
import uz.pdp.inventorymanagementsystem.model.UoM
import uz.pdp.inventorymanagementsystem.repo.UoMRepo
import java.time.Instant
import java.util.UUID

@Service
class UoMService(
    override val repository: UoMRepo,
    override val mapper: UoMMapper
) : BaseServiceImpl<
        UoM,
        UoMCreateDTO,
        UoMUpdateDTO,
        UoMResponseDTO,
        UoMMapper,
        UoMRepo
        >(repository, mapper) {


    override fun create(dto: UoMCreateDTO): UoMResponseDTO {
        validateCreate(dto)

        val uom = convertCreateDtoToEntity(dto)

        val saved = saveAndRefresh(uom)
        return mapper.toDTO(saved)
    }

    override fun getById(id: UUID): UoMResponseDTO {
        val uom = getByIdOrThrow(id)
        return mapper.toDTO(uom)
    }

    override fun update(id: UUID, dto: UoMUpdateDTO): UoMResponseDTO {
        val uom = getActive(id)

        validateUpdate(id, dto)
        updateEntityFromDto(uom, dto)

        val updated = saveAndRefresh(uom)
        return mapper.toDTO(updated)
    }

    override fun delete(id: UUID) {
        getByIdOrThrow(id) // to throw exception if not found
        validateDelete(id) // kerak bo'lsa
        repository.trash(id)
    }

    /* ===================== HELPERS ===================== */
    fun getByIdOrThrow(id: UUID): UoM {
        return getEntityOrNull(id) ?: throw UoMNotFoundException(
            "UoM with id '$id' not found."
        )
    }

    fun getActive(id: UUID): UoM =
        getByIdOrThrow(id).apply {
            if (status != Status.ACTIVE) {
                throw ValidationException(mapOf(
                    "uom" to "UoM with id '${this.id}' is INACTIVE."
                ))
            }
        }

    /* ===================== MAPPING ===================== */
    override fun convertCreateDtoToEntity(dto: UoMCreateDTO): UoM {
        return UoM().apply {
            code = dto.code
            name = dto.name
        }
    }

    override fun updateEntityFromDto(
        entity: UoM,
        dto: UoMUpdateDTO
    ) {
        dto.code?.let { entity.code = it }
        dto.name?.let { entity.name = it }
        dto.updatedAt = Instant.now()
    }

    /* ===================== VALIDATION ===================== */

    override fun validateCreate(dto: UoMCreateDTO) {
        val errors = mutableMapOf<String, String>()

        dto.code.takeIf { repository.existsByCodeAndDeletedFalse(it) }
            ?.let { code -> errors["code"] = "UoM with code '$code' already exists" }

        if (errors.isNotEmpty()) throw ValidationException(errors)
    }

    override fun validateUpdate(id: UUID, dto: UoMUpdateDTO) {
        val current = getByIdOrThrow(id)

        val errors = mutableMapOf<String, String>()

        dto.code?.let { code ->
            repository.findByCodeAndDeletedFalse(code)
                ?.takeIf { it.id != current.id }
                ?.let { errors["code"] = "UoM with code '$code' already exists" }
        }

        if (errors.isNotEmpty()) throw ValidationException(errors)
    }

}
