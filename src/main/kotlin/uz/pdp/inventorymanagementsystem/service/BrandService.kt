package uz.pdp.inventorymanagementsystem.service

import org.springframework.stereotype.Service
import uz.pdp.inventorymanagementsystem.base.BaseServiceImpl
import uz.pdp.inventorymanagementsystem.dto.*
import uz.pdp.inventorymanagementsystem.enums.Status
import uz.pdp.inventorymanagementsystem.exception.BrandNotFoundException
import uz.pdp.inventorymanagementsystem.exception.ValidationException
import uz.pdp.inventorymanagementsystem.mapper.BrandMapper
import uz.pdp.inventorymanagementsystem.model.Brand
import uz.pdp.inventorymanagementsystem.repo.BrandRepo
import java.util.*

@Service
class BrandService(
    override val repository: BrandRepo,
    override val mapper: BrandMapper
) : BaseServiceImpl<
        Brand,
        BrandCreateDTO,
        BrandUpdateDTO,
        BrandResponseDTO,
        BrandMapper,
        BrandRepo
        >(repository, mapper) {

    override fun create(dto: BrandCreateDTO): BrandResponseDTO {
        validateCreate(dto)

        val brand = convertCreateDtoToEntity(dto)

        val saved = saveAndRefresh(brand)
        return mapper.toDTO(saved)
    }

    // controller uchun
    override fun getById(id: UUID): BrandResponseDTO {
        val brand = getByIdOrThrow(id) // (active and inactive)
        return mapper.toDTO(brand)
    }

    override fun update(id: UUID, dto: BrandUpdateDTO): BrandResponseDTO {
        val brand = getActive(id)

        validateUpdate(id, dto)
        updateEntityFromDto(brand, dto)

        val updated = saveAndRefresh(brand)
        return mapper.toDTO(updated)
    }

    override fun delete(id: UUID) {
        getByIdOrThrow(id) // to throw exception if not found
        validateDelete(id) // keyinchalik kerak bo'lsa
        repository.trash(id)
    }

    /* ===================== HELPERS ===================== */

    fun getActive(id: UUID): Brand =
        getByIdOrThrow(id).apply {
            if (status != Status.ACTIVE)
                throw ValidationException(mapOf("brand" to "Brand with id '$id' is INACTIVE"))
        }

    // active and inactive brands for controller
    fun getByIdOrThrow(id: UUID): Brand {
        return getEntityOrNull(id) ?: throw BrandNotFoundException(
            "Brand with id '$id' not found."
        )
    }


    /* ===================== MAPPING ===================== */

    override fun convertCreateDtoToEntity(dto: BrandCreateDTO): Brand {
        return Brand().apply {
            name = dto.name
            country = dto.country
        }
    }

    override fun updateEntityFromDto(entity: Brand, dto: BrandUpdateDTO) {
        dto.name?.let { entity.name = it }
        dto.country?.let { entity.country = it }
    }

    /* ===================== VALIDATION ===================== */

    override fun validateCreate(dto: BrandCreateDTO) {
        dto.name.takeIf { repository.existsByNameAndDeletedFalse(it) }
            ?.let { name ->
                throw ValidationException(
                    mapOf(
                        "name" to "Brand with name '$name' already exists"
                    )
                )
            }
    }

    override fun validateUpdate(id: UUID, dto: BrandUpdateDTO) {
        val current = getByIdOrThrow(id) // inactive ones can also be updated

        dto.name?.let { name ->
            repository.findByNameAndDeletedFalse(name)
                ?.takeIf { it.id != current.id }
                ?.let {
                    throw ValidationException(
                        mapOf(
                            "name" to "Brand with name '$name' already exists"
                        )
                    )
                }
        }
    }
}