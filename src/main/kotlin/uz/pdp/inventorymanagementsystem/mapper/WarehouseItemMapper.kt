package uz.pdp.inventorymanagementsystem.mapper

import org.springframework.stereotype.Component
import uz.pdp.inventorymanagementsystem.base.BaseMapper
import uz.pdp.inventorymanagementsystem.dto.WarehouseItemCreateDTO
import uz.pdp.inventorymanagementsystem.dto.WarehouseItemResponseDTO
import uz.pdp.inventorymanagementsystem.model.Product
import uz.pdp.inventorymanagementsystem.model.WarehouseItem
import uz.pdp.inventorymanagementsystem.model.WarehouseTransaction

@Component
class WarehouseItemMapper(
    private val productMapper: ProductMapper
) : BaseMapper<WarehouseItem, WarehouseItemResponseDTO> {

    override fun toDTO(entity: WarehouseItem): WarehouseItemResponseDTO {
        return WarehouseItemResponseDTO(
            transactionId = entity.transaction.id,
            product = entity.product?.let { productMapper.toDTO(it) },
            quantity = entity.quantity,
            unitPrice = entity.unitPrice,
            totalPrice = entity.totalPrice,
            expireAt = entity.expireAt
        ).apply {
            mapBaseFields(entity, this)
        }
    }

    fun toEntity(
        dto: WarehouseItemCreateDTO,
        product: Product,
        transaction: WarehouseTransaction
    ): WarehouseItem {
        return WarehouseItem().apply {
            this.product = product
            this.transaction = transaction
            this.quantity = dto.quantity
            this.unitPrice = dto.unitPrice
            this.totalPrice = unitPrice.multiply(quantity)
            this.expireAt = dto.expireAt
        }
    }
}