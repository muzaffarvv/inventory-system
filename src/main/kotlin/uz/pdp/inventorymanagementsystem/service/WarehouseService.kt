package uz.pdp.inventorymanagementsystem.service

import org.springframework.stereotype.Service
import uz.pdp.inventorymanagementsystem.base.BaseServiceImpl
import uz.pdp.inventorymanagementsystem.dto.WarehouseCreateDTO
import uz.pdp.inventorymanagementsystem.dto.WarehouseResponseDTO
import uz.pdp.inventorymanagementsystem.dto.WarehouseUpdateDTO
import uz.pdp.inventorymanagementsystem.enums.Status
import uz.pdp.inventorymanagementsystem.exception.ValidationException
import uz.pdp.inventorymanagementsystem.exception.WarehouseNotFoundException
import uz.pdp.inventorymanagementsystem.mapper.WarehouseMapper
import uz.pdp.inventorymanagementsystem.model.Warehouse
import uz.pdp.inventorymanagementsystem.repo.WarehouseRepo
import uz.pdp.inventorymanagementsystem.utils.CodeGenerator
import java.time.Instant
import java.util.UUID

@Service
class WarehouseService(
    override val repository: WarehouseRepo,
    override val mapper: WarehouseMapper
) : BaseServiceImpl<
        Warehouse,
        WarehouseCreateDTO,
        WarehouseUpdateDTO,
        WarehouseResponseDTO,
        WarehouseMapper,
        WarehouseRepo
        >(repository, mapper) {


    override fun create(dto: WarehouseCreateDTO): WarehouseResponseDTO {
        validateCreate(dto)

        val warehouse = convertCreateDtoToEntity(dto)

        val saved = saveAndRefresh(warehouse)
        return mapper.toDTO(saved)
    }


    override fun getById(id: UUID): WarehouseResponseDTO {
        val warehouse = getByIdOrThrow(id) // (active and inactive)
        return mapper.toDTO(warehouse)
    }

    override fun update(id: UUID, dto: WarehouseUpdateDTO): WarehouseResponseDTO {
        val warehouse = getByIdOrThrow(id)

        validateUpdate(id, dto)
        updateEntityFromDto(warehouse, dto)

        val updated = saveAndRefresh(warehouse)
        return mapper.toDTO(updated)
    }

    override fun delete(id: UUID) {
        getByIdOrThrow(id) // to throw exception if not found
        validateDelete(id) // keyinchalik kerak bo'lsa
        repository.trash(id)
    }

    /* ===================== HELPERS ===================== */

    fun getByIdOrThrow(id: UUID): Warehouse =
        getEntityOrNull(id) ?: throw WarehouseNotFoundException(
            "Warehouse with id '$id' not found."
        )

    fun getActive(id: UUID): Warehouse =
        getByIdOrThrow(id).apply {
            if (status != Status.ACTIVE) {
                throw ValidationException(
                    mapOf(
                        "warehouse" to "Warehouse with id '$id' is INACTIVE."
                    )
                )
            }
        }

    private fun getCodeLastNumber(): Int {
        val lastCode = repository.findTopByOrderByCodeDesc()?.code ?: return 0
        // WH-001 → oxirgi 3 raqamni olish
        return lastCode.takeLast(3).toInt()
    }

    /* ===================== MAPPING ===================== */

    override fun convertCreateDtoToEntity(dto: WarehouseCreateDTO): Warehouse {
        return Warehouse().apply {
            name = dto.name
            address = dto.address
            code = CodeGenerator.forWarehouse(getCodeLastNumber())
        }
    }

    override fun updateEntityFromDto(
        entity: Warehouse,
        dto: WarehouseUpdateDTO
    ) {
        dto.name?.let { entity.name = it }
        dto.address?.let { entity.address = it }
        dto.updatedAt = Instant.now()
    }

    /* ===================== VALIDATION ===================== */

    override fun validateCreate(dto: WarehouseCreateDTO) {
        val errors = mutableMapOf<String, String>()

        dto.name.takeIf { repository.existsByNameAndDeletedFalse(it) }
            ?.let { name ->
                errors["name"] = "Warehouse with name '$name' already exists."
            }

        dto.address?.let { address ->
            if (repository.existsByAddressAndDeletedFalse(address)) {
                errors["address"] = "Warehouse with address '$address' already exists."
            }
        }

        if (errors.isNotEmpty()) {
            throw ValidationException(errors)
        }
    }


    override fun validateUpdate(id: UUID, dto: WarehouseUpdateDTO) {
        val current = getByIdOrThrow(id) // inactive ones can also be updated

        val errors = mutableMapOf<String, String>()

        dto.name?.let { name ->
            repository.findByNameAndDeletedFalse(name)
                ?.takeIf { it.id != current.id }
                ?.let { errors["name"] = "Warehouse with name '$name' already exists." }
        }

        dto.address?.let { address ->
            repository.findByAddressAndDeletedFalse(address)
                ?.takeIf { it.id != current.id }
                ?.let { errors["address"] = "Warehouse with address '$address' already exists." }
        }

        if (errors.isNotEmpty()) ValidationException(errors)
    }

}