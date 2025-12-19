package uz.pdp.inventorymanagementsystem.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uz.pdp.inventorymanagementsystem.dto.*
import uz.pdp.inventorymanagementsystem.enums.WarehouseTransactionType
import uz.pdp.inventorymanagementsystem.exception.ValidationException
import uz.pdp.inventorymanagementsystem.mapper.WarehouseTransactionMapper
import uz.pdp.inventorymanagementsystem.model.WarehouseItem
import uz.pdp.inventorymanagementsystem.model.WarehouseTransaction
import uz.pdp.inventorymanagementsystem.repo.WarehouseTransactionRepo
import java.math.BigDecimal
import java.util.UUID

@Service
class WarehouseTransactionService(
    private val repository: WarehouseTransactionRepo,
    private val mapper: WarehouseTransactionMapper,
    private val warehouseService: WarehouseService,
    private val productService: ProductService
) {

    @Transactional
    fun create(dto: WarehouseTransactionCreateDTO): WarehouseTransactionResponseDTO {
        validateCreate(dto)
        val warehouse = warehouseService.getActive(dto.warehouseId)

        val transaction = WarehouseTransaction().apply {
            date = dto.date
            description = dto.description
            type = dto.type
            factoryNumber = dto.factoryNumber
            this.warehouse = warehouse
        }

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

        val saved = repository.save(transaction)
        repository.flush()
        return mapper.toDTO(saved)
    }

    fun getById(id: UUID): WarehouseTransactionResponseDTO =
        mapper.toDTO(getByIdOrThrow(id))

    fun getAll(): List<WarehouseTransactionResponseDTO> =
        repository.findAllByDeletedFalse().map(mapper::toDTO)

    fun getByWarehouseId(warehouseId: UUID): List<WarehouseTransactionResponseDTO> =
        repository.findAllByWarehouse_IdAndDeletedFalse(warehouseId).map(mapper::toDTO)

    fun getByType(type: WarehouseTransactionType): List<WarehouseTransactionResponseDTO> =
        repository.findAllByTypeAndDeletedFalse(type).map(mapper::toDTO)

    @Transactional
    fun update(id: UUID, dto: WarehouseTransactionUpdateDTO): WarehouseTransactionResponseDTO {
        val transaction = getByIdOrThrow(id)
        validateUpdate(id, dto)

        dto.date?.let { transaction.date = it }
        dto.description?.let { transaction.description = it }
        dto.type?.let { transaction.type = it }
        dto.factoryNumber?.let { transaction.factoryNumber = it }
        dto.warehouseId?.let { transaction.warehouse = warehouseService.getActive(it) }

        val updated = repository.save(transaction)
        repository.flush()
        return mapper.toDTO(updated)
    }

    @Transactional
    fun delete(id: UUID) {
        getByIdOrThrow(id)
        repository.trash(id)
    }

    fun getByIdOrThrow(id: UUID): WarehouseTransaction =
        repository.findByIdAndDeletedFalse(id)
            ?: throw ValidationException(
                mapOf("transaction" to "Transaction with id '$id' not found")
            )

    private fun validateCreate(dto: WarehouseTransactionCreateDTO) {
        val errors = mutableMapOf<String, String>()

        runCatching { warehouseService.getActive(dto.warehouseId) }
            .onFailure { errors["warehouse"] = "Warehouse not found or inactive" }

        if (repository.existsByFactoryNumberAndDeletedFalse(dto.factoryNumber))
            errors["factoryNumber"] = "Factory number '${dto.factoryNumber}' already exists"

        if (dto.items.isEmpty())
            errors["items"] = "Transaction must have at least one item"

        val productIds = dto.items.map { it.productId }
        if (productIds.size != productIds.distinct().size)
            errors["items"] = "Duplicate products are not allowed"

        if (errors.isNotEmpty()) throw ValidationException(errors)
    }

    private fun validateItem(dto: WarehouseItemCreateDTO) {
        val errors = mutableMapOf<String, String>()

        runCatching { productService.getActive(dto.productId) }
            .onFailure { errors["product_${dto.productId}"] = "Product not found or inactive" }

        if (dto.quantity <= BigDecimal.ZERO)
            errors["quantity"] = "Quantity must be > 0"
        if (dto.unitPrice <= BigDecimal.ZERO)
            errors["unitPrice"] = "Unit price must be > 0"

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