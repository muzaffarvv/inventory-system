package uz.pdp.inventorymanagementsystem.dto

import uz.pdp.inventorymanagementsystem.base.BaseDTO
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class WarehouseItemResponseDTO(
    var transactionId: UUID? = null,
    var product: ProductResponseDTO? = null,
    var quantity: BigDecimal = BigDecimal.ZERO,
    var unitPrice: BigDecimal = BigDecimal.ZERO,
    var totalPrice: BigDecimal = BigDecimal.ZERO,
    var expireAt: Instant? = null
) : BaseDTO()

data class WarehouseItemCreateDTO(
    var productId: UUID,
    var quantity: BigDecimal,
    var unitPrice: BigDecimal,
    var expireAt: Instant? = null
)

data class WarehouseItemUpdateDTO(
    var productId: UUID? = null,
    var quantity: BigDecimal? = null,
    var unitPrice: BigDecimal? = null,
    var expireAt: Instant? = null
)