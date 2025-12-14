package uz.pdp.inventorymanagementsystem.mapper

import org.springframework.stereotype.Component
import uz.pdp.inventorymanagementsystem.base.BaseMapper
import uz.pdp.inventorymanagementsystem.dto.WarehouseResponseDTO
import uz.pdp.inventorymanagementsystem.model.Warehouse

@Component
class WarehouseMapper : BaseMapper<Warehouse, WarehouseResponseDTO> {

    override fun toDTO(entity: Warehouse): WarehouseResponseDTO {
        return WarehouseResponseDTO(
            name = entity.name,
            address = entity.address
        ).apply {
            mapBaseFields(entity, this)
        }
    }

    fun toEntity(dto: WarehouseResponseDTO): Warehouse {
        return Warehouse().apply {
            name = dto.name
            address = dto.address
        }
    }
}