package uz.pdp.inventorymanagementsystem.mapper

import org.springframework.stereotype.Component
import uz.pdp.inventorymanagementsystem.base.BaseMapper
import uz.pdp.inventorymanagementsystem.dto.WarehouseTransactionCreateDTO
import uz.pdp.inventorymanagementsystem.dto.WarehouseTransactionResponseDTO
import uz.pdp.inventorymanagementsystem.model.Warehouse
import uz.pdp.inventorymanagementsystem.model.WarehouseItem
import uz.pdp.inventorymanagementsystem.model.WarehouseTransaction

@Component
class WarehouseTransactionMapper(
    private val warehouseMapper: WarehouseMapper,
    private val warehouseItemMapper: WarehouseItemMapper
) : BaseMapper<WarehouseTransaction, WarehouseTransactionResponseDTO> {

    override fun toDTO(entity: WarehouseTransaction): WarehouseTransactionResponseDTO {
        return WarehouseTransactionResponseDTO(
            date = entity.date,
            description = entity.description,
            type = entity.type,
            factoryNumber = entity.factoryNumber,
            warehouseResponseDTO = warehouseMapper.toDTO(entity.warehouse),
            items = entity.items.map { warehouseItemMapper.toDTO(it) }
        ).apply {
            mapBaseFields(entity, this)
        }
    }

    fun toEntity(
        dto: WarehouseTransactionCreateDTO,
        warehouse: Warehouse,
        items: List<WarehouseItem>
    ): WarehouseTransaction {
        return WarehouseTransaction().apply {
            date = dto.date
            description = dto.description
            type = dto.type
            factoryNumber = dto.factoryNumber
            this.warehouse = warehouse
            this.items = items.toMutableList()
        }
    }

}