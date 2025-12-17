package uz.pdp.inventorymanagementsystem.service

import org.springframework.stereotype.Service
import uz.pdp.inventorymanagementsystem.base.BaseServiceImpl
import uz.pdp.inventorymanagementsystem.dto.WarehouseItemCreateDTO
import uz.pdp.inventorymanagementsystem.dto.WarehouseItemResponseDTO
import uz.pdp.inventorymanagementsystem.dto.WarehouseItemUpdateDTO
import uz.pdp.inventorymanagementsystem.exception.ValidationException
import uz.pdp.inventorymanagementsystem.mapper.WarehouseItemMapper
import uz.pdp.inventorymanagementsystem.model.WarehouseItem
import uz.pdp.inventorymanagementsystem.repo.WarehouseItemRepo
import uz.pdp.inventorymanagementsystem.repo.WarehouseTransactionRepo
import java.math.BigDecimal
import java.util.UUID

@Service
class WarehouseItemService(
    override val repository: WarehouseItemRepo,
    override val mapper: WarehouseItemMapper,
    private val productService: ProductService,
    private val transactionRepo: WarehouseTransactionRepo
) : BaseServiceImpl<
        WarehouseItem,
        WarehouseItemCreateDTO,
        WarehouseItemUpdateDTO,
        WarehouseItemResponseDTO,
        WarehouseItemMapper,
        WarehouseItemRepo
        >(repository, mapper) {

    /* ===================== CRUD ===================== */
    // Bu metod faqat test/debug uchun
    // Real scenarioda itemlar TransactionService orqali yaratiladi

    override fun create(dto: WarehouseItemCreateDTO): WarehouseItemResponseDTO {
        throw UnsupportedOperationException(
            "Items should be created through WarehouseTransactionService"
        )
    }

    override fun getById(id: UUID): WarehouseItemResponseDTO =
        mapper.toDTO(getByIdOrThrow(id))

    override fun update(id: UUID, dto: WarehouseItemUpdateDTO): WarehouseItemResponseDTO {
        val item = getByIdOrThrow(id)
        validateUpdate(id, dto)
        updateEntityFromDto(item, dto)
        return mapper.toDTO(saveAndRefresh(item))
    }

    override fun delete(id: UUID) {
        getByIdOrThrow(id)
        validateDelete(id)
        repository.trash(id)
    }

    /* ===================== QUERIES ===================== */

    fun getByWarehouseId(warehouseId: UUID): Set<WarehouseItemResponseDTO> =
        repository.findAllByTransaction_Warehouse_IdAndDeletedFalse(warehouseId)
            .map(mapper::toDTO)
            .toSet()

    fun getByTransactionId(transactionId: UUID): List<WarehouseItemResponseDTO> =
        repository.findAllByTransaction_IdAndDeletedFalse(transactionId)
            .map(mapper::toDTO)

    fun getExpiringSoon(days: Int = 30): List<WarehouseItemResponseDTO> {
        val now = java.time.Instant.now()
        val futureDate = now.plusSeconds(days * 24 * 60 * 60L)

        return repository.findAllWithExpiration()
            .filter { it.expireAt != null && it.expireAt!! in now..futureDate }
            .map(mapper::toDTO)
    }

    /* ===================== HELPERS ===================== */

    fun getByIdOrThrow(id: UUID): WarehouseItem =
        getEntityOrNull(id) ?: throw ValidationException(
            mapOf("warehouseItem" to "WarehouseItem with id '$id' not found")
        )

    fun getActive(id: UUID): WarehouseItem = getByIdOrThrow(id)

    /* ===================== MAPPING ===================== */

    override fun convertCreateDtoToEntity(dto: WarehouseItemCreateDTO): WarehouseItem {
        throw UnsupportedOperationException(
            "Use mapper.toEntity() with transaction parameter instead"
        )
    }

    override fun updateEntityFromDto(
        entity: WarehouseItem,
        dto: WarehouseItemUpdateDTO
    ) {
        dto.quantity?.let {
            entity.quantity = it
            entity.totalPrice = entity.unitPrice.multiply(it)
        }

        dto.unitPrice?.let {
            entity.unitPrice = it
            entity.totalPrice = it.multiply(entity.quantity)
        }

        dto.expireAt?.let { entity.expireAt = it }

        dto.productId?.let {
            entity.product = productService.getActive(it)
        }
    }

    /* ===================== VALIDATION ===================== */

    override fun validateCreate(dto: WarehouseItemCreateDTO) {
        throw UnsupportedOperationException(
            "Validation is done in WarehouseTransactionService"
        )
    }

    override fun validateUpdate(id: UUID, dto: WarehouseItemUpdateDTO) {
        getByIdOrThrow(id)

        val errors = mutableMapOf<String, String>()

        dto.quantity?.takeIf { it <= BigDecimal.ZERO }
            ?.let { errors["quantity"] = "Quantity must be greater than zero" }

        dto.unitPrice?.takeIf { it <= BigDecimal.ZERO }
            ?.let { errors["unitPrice"] = "Unit price must be greater than zero" }

        dto.productId?.let {
            runCatching { productService.getActive(it) }
                .onFailure { errors["product"] = "Product not found or inactive" }
        }

        if (errors.isNotEmpty()) throw ValidationException(errors)
    }

    override fun validateDelete(id: UUID) {
        // Transaction locked bo'lsa, item o'chirishni taqiqlash
    }
}