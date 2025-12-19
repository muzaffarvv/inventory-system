import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uz.pdp.inventorymanagementsystem.dto.*
import uz.pdp.inventorymanagementsystem.enums.WarehouseTransactionType
import uz.pdp.inventorymanagementsystem.exception.ValidationException
import uz.pdp.inventorymanagementsystem.mapper.WarehouseTransactionMapper
import uz.pdp.inventorymanagementsystem.model.WarehouseItem
import uz.pdp.inventorymanagementsystem.model.WarehouseTransaction
import uz.pdp.inventorymanagementsystem.repo.WarehouseTransactionRepo
import uz.pdp.inventorymanagementsystem.service.ProductService
import uz.pdp.inventorymanagementsystem.service.WarehouseService
import java.math.BigDecimal
import java.util.UUID

@Service
class WarehouseTransactionService(
    private val repository: WarehouseTransactionRepo,
    private val mapper: WarehouseTransactionMapper,
    private val warehouseService: WarehouseService,
    private val productService: ProductService
) {

    /* ===================== CREATE ===================== */
    @Transactional
    fun create(dto: WarehouseTransactionCreateDTO): WarehouseTransactionResponseDTO {
        validateCreate(dto) // DTO va itemlarni tekshirish

        val warehouse = warehouseService.getActive(dto.warehouseId) // Ombor tekshirish

        // Transaction entity yaratish
        val transaction = WarehouseTransaction().apply {
            date = dto.date
            description = dto.description
            type = dto.type
            factoryNumber = dto.factoryNumber
            this.warehouse = warehouse
        }

        // Itemlarni yaratish va validatsiya
        transaction.items = dto.items.map { itemDto ->
            validateItem(itemDto)
            val product = productService.getActive(itemDto.productId)
            WarehouseItem().apply {
                this.transaction = transaction
                this.product = product
                this.quantity = itemDto.quantity
                this.unitPrice = itemDto.unitPrice
                this.totalPrice = unitPrice.multiply(quantity)
                this.expireAt = itemDto.expireAt
            }
        }.toMutableList()

        val saved = repository.save(transaction) // DB ga saqlash
        repository.flush()

        return mapper.toDTO(saved) // Response DTO ga o'tkazish
    }

    /* ===================== READ ===================== */
    fun getById(id: UUID): WarehouseTransactionResponseDTO =
        mapper.toDTO(getByIdOrThrow(id)) // ID bo'yicha olish

    fun getAll(): List<WarehouseTransactionResponseDTO> =
        repository.findAllByDeletedFalse().map(mapper::toDTO)

    fun getByWarehouseId(warehouseId: UUID): List<WarehouseTransactionResponseDTO> =
        repository.findAllByWarehouse_IdAndDeletedFalse(warehouseId).map(mapper::toDTO)

    // INBOUND, OUTBOUND
    fun getByType(type: WarehouseTransactionType): List<WarehouseTransactionResponseDTO> =
        repository.findAllByTypeAndDeletedFalse(type).map(mapper::toDTO)

    /* ===================== UPDATE ===================== */
    @Transactional
    fun update(id: UUID, dto: WarehouseTransactionUpdateDTO): WarehouseTransactionResponseDTO {
        val transaction = getByIdOrThrow(id)
        validateUpdate(id, dto)

        // DTO dagi mavjud qiymatlarni entity ga qo‘llash
        dto.date?.let { transaction.date = it }
        dto.description?.let { transaction.description = it }
        dto.type?.let { transaction.type = it }
        dto.factoryNumber?.let { transaction.factoryNumber = it }
        dto.warehouseId?.let { transaction.warehouse = warehouseService.getActive(it) }

        val updated = repository.save(transaction)
        repository.flush()

        return mapper.toDTO(updated)
    }

    /* ===================== DELETE ===================== */
    @Transactional
    fun delete(id: UUID) {
        getByIdOrThrow(id)
       // validateDelete(id)  qoida qo‘shish mumkin
        repository.trash(id)
    }

    /* ===================== HELPERS ===================== */
    fun getByIdOrThrow(id: UUID): WarehouseTransaction =
        repository.findByIdAndDeletedFalse(id)
            ?: throw ValidationException(
                mapOf("transaction" to "Transaction with id '$id' not found")
            )

    /* ===================== VALIDATION ===================== */
    private fun validateCreate(dto: WarehouseTransactionCreateDTO) {
        val errors = mutableMapOf<String, String>()

        // Ombor mavjudligini tekshirish
        runCatching { warehouseService.getActive(dto.warehouseId) }
            .onFailure { errors["warehouse"] = "Warehouse not found or inactive" }

        // Faktura raqamining takrorlanmasligini tekshirish
        if (repository.existsByFactoryNumberAndDeletedFalse(dto.factoryNumber))
            errors["factoryNumber"] = "Factory number '${dto.factoryNumber}' already exists"

        // Itemlar bo‘sh bo'lmasligi kerak
        if (dto.items.isEmpty()) errors["items"] = "Transaction must have at least one item"

        // Duplicate product yo‘qligini tekshirish
        val productIds = dto.items.map { it.productId }
        if (productIds.size != productIds.distinct().size)
            errors["items"] = "Duplicate products are not allowed"

        if (errors.isNotEmpty()) throw ValidationException(errors)
    }

    private fun validateItem(dto: WarehouseItemCreateDTO) {
        val errors = mutableMapOf<String, String>()
        runCatching { productService.getActive(dto.productId) }
            .onFailure { errors["product_${dto.productId}"] = "Product not found or inactive" }

        if (dto.quantity <= BigDecimal.ZERO) errors["quantity"] = "Quantity must be > 0"
        if (dto.unitPrice <= BigDecimal.ZERO) errors["unitPrice"] = "Unit price must be > 0"

        if (errors.isNotEmpty()) throw ValidationException(errors)
    }

    private fun validateUpdate(id: UUID, dto: WarehouseTransactionUpdateDTO) {
        val errors = mutableMapOf<String, String>()

        dto.warehouseId?.let {
            runCatching { warehouseService.getActive(it) }
                .onFailure { errors["warehouse"] = "Warehouse not found or inactive" }
        }

        dto.factoryNumber?.let { factoryNum ->
            repository.findByFactoryNumberAndDeletedFalse(factoryNum)
                ?.takeIf { it.id != id }
                ?.let { errors["factoryNumber"] = "Factory number '$factoryNum' already exists" }
        }

        if (errors.isNotEmpty()) throw ValidationException(errors)
    }
}
