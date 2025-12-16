package uz.pdp.inventorymanagementsystem.service

import org.springframework.stereotype.Service
import uz.pdp.inventorymanagementsystem.base.BaseServiceImpl
import uz.pdp.inventorymanagementsystem.dto.*
import uz.pdp.inventorymanagementsystem.exception.BrandAlreadyExistsException
import uz.pdp.inventorymanagementsystem.exception.BrandNotFoundException
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

    override fun getById(id: UUID): BrandResponseDTO {
        val brand = getBrandOrThrow(id)
        return mapper.toDTO(brand)
    }

    override fun update(id: UUID, dto: BrandUpdateDTO): BrandResponseDTO {
        val brand = getBrandOrThrow(id)

        validateUpdate(id, dto)
        updateEntityFromDto(brand, dto)

        val updated = saveAndRefresh(brand)
        return mapper.toDTO(updated)
    }

    override fun delete(id: UUID) {
        getBrandOrThrow(id) // to throw exception if not found
        validateDelete(id)
        repository.trash(id)
    }

    /* ===================== HELPERS ===================== */

    private fun getBrandOrThrow(id: UUID): Brand =
        getEntityOrNull(id) ?: throw BrandNotFoundException(id)

    /* ===================== MAPPING ===================== */

    override fun convertCreateDtoToEntity(dto: BrandCreateDTO): Brand =
        Brand().apply {
            name = dto.name
            country = dto.country
        }

    override fun updateEntityFromDto(entity: Brand, dto: BrandUpdateDTO) {
        dto.name?.let { entity.name = it }
        dto.country?.let { entity.country = it }
    }

    /* ===================== VALIDATION ===================== */

    override fun validateCreate(dto: BrandCreateDTO) {
        if (repository.existsByNameAndDeletedFalse(dto.name)) {
            throw BrandAlreadyExistsException(
                "Brand with name '${dto.name}' already exists"
            )
        }
    }

    override fun validateUpdate(id: UUID, dto: BrandUpdateDTO) {
        dto.name?.let { name ->
            repository.findByNameAndDeletedFalse(name)?.let { existing ->
                if (existing.id != id) {
                    throw BrandAlreadyExistsException(
                        "Brand with name '$name' already exists"
                    )
                }
            }
        }
    }

}
