package uz.pdp.inventorymanagementsystem.service

import org.springframework.stereotype.Service
import uz.pdp.inventorymanagementsystem.base.BaseServiceImpl
import uz.pdp.inventorymanagementsystem.dto.SupplierCreateDTO
import uz.pdp.inventorymanagementsystem.dto.SupplierResponseDTO
import uz.pdp.inventorymanagementsystem.dto.SupplierUpdateDTO
import uz.pdp.inventorymanagementsystem.exception.SupplierNotFoundException
import uz.pdp.inventorymanagementsystem.exception.ValidationException
import uz.pdp.inventorymanagementsystem.mapper.SupplierMapper
import uz.pdp.inventorymanagementsystem.model.Supplier
import uz.pdp.inventorymanagementsystem.repo.SupplierRepo
import java.time.Instant
import java.util.UUID

@Service
class SupplierService(
    override val repository: SupplierRepo,
    override val mapper: SupplierMapper,
    private val brandService: BrandService,
    private val warehouseService: WarehouseService
) : BaseServiceImpl<
        Supplier,
        SupplierCreateDTO,
        SupplierUpdateDTO,
        SupplierResponseDTO,
        SupplierMapper,
        SupplierRepo
        >(repository, mapper) {


    override fun create(dto: SupplierCreateDTO): SupplierResponseDTO {
        validateCreate(dto)

        val supplier = convertCreateDtoToEntity(dto)

        val saved = saveAndRefresh(supplier)
        return mapper.toDTO(saved)
    }


    override fun getById(id: UUID): SupplierResponseDTO {
        val supplier = getByIdOrThrow(id)
        return mapper.toDTO(supplier)
    }

    override fun update(id: UUID, dto: SupplierUpdateDTO): SupplierResponseDTO {
        val supplier = getByIdOrThrow(id)

        validateUpdate(id, dto)
        updateEntityFromDto(supplier, dto)

        val updated = saveAndRefresh(supplier)
        return mapper.toDTO(updated)
    }

    override fun delete(id: UUID) {
        getByIdOrThrow(id) // to throw if not found
        validateDelete(id)
        repository.deleteById(id)
    }

    /* ===================== HELPERS ===================== */

    fun getByIdOrThrow(id: UUID): Supplier {
        return getEntityOrNull(id) ?: throw SupplierNotFoundException(
            "Supplier with id '$id' not found."
        )
    }

    fun getWarehouseId(id: UUID): Set<SupplierResponseDTO> {
        warehouseService.getActive(id)
        // non deleted
        return repository.findSuppliersByWarehouseIdAndDeletedFalse(id)
            .map ( mapper::toDTO)
            .toSet()
    }

    fun getByBrandId(id: UUID): Set<SupplierResponseDTO> {
        brandService.getActive(id)
        // non deleted
        return repository.findSuppliersByBrandIdAndDeletedFalse(id)
            .map(mapper::toDTO)
            .toSet()
    }

    /* ===================== MAPPING ===================== */

    override fun convertCreateDtoToEntity(dto: SupplierCreateDTO): Supplier {
        val brand = brandService.getActive(dto.brandId)
        val warehouse = warehouseService.getActive(dto.warehouseId)

        return Supplier().apply {
            about = dto.about
            phone = dto.phone
            this.brand = brand
            this.warehouse = warehouse
        }
    }

    override fun updateEntityFromDto(
        entity: Supplier,
        dto: SupplierUpdateDTO
    ) {
        dto.phone?.let { entity.phone = it }
        dto.about?.let { entity.about = it }

        dto.brandId?.let {
            entity.brand = brandService.getActive(it)
        }

        dto.warehouseId?.let {
            entity.warehouse = warehouseService.getActive(it)
        }
    }

    override fun validateCreate(dto: SupplierCreateDTO) {
        val errors = mutableMapOf<String, String>()

        // brand active tekshirish
        runCatching { brandService.getActive(dto.brandId) }
            .onFailure { errors["brand"] = "Brand with id '${dto.brandId}' is not ACTIVE" }

        // warehouse active tekshirish
        runCatching { warehouseService.getActive(dto.warehouseId) }
            .onFailure { errors["warehouse"] = "Warehouse with id '${dto.warehouseId}' is not ACTIVE" }

        // phone unique
        dto.phone.takeIf { repository.existsByPhoneAndDeletedFalse(it) }
            ?.let { phone ->
                errors["phone"] = "Supplier with phone '$phone' already exists"
            }

        if (errors.isNotEmpty()) throw ValidationException(errors)
    }


    override fun validateUpdate(id: UUID, dto: SupplierUpdateDTO) {
        val current = getByIdOrThrow(id)

        val errors = mutableMapOf<String, String>()

        dto.phone?.let { phone ->
            repository.findByPhoneAndDeletedFalse(phone)
                ?.takeIf { it.id != current.id }
                ?.let { errors["phone"] = "Supplier with phone '$phone' already exists" }
        }

        dto.brandId?.let { it ->
            runCatching { brandService.getActive(it) }
                .onFailure { errors["brand"] = "Brand with id '$it' is not ACTIVE" }
        }

        dto.warehouseId?.let { it ->
            runCatching { warehouseService.getActive(it) }
                .onFailure { errors["warehouse"] = "Warehouse with id '$it' is not ACTIVE" }
        }

        if (errors.isNotEmpty()) throw ValidationException(errors)
    }



}
