package uz.pdp.inventorymanagementsystem.mapper

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import uz.pdp.inventorymanagementsystem.base.BaseMapper
import uz.pdp.inventorymanagementsystem.dto.WarehouseCreateDTO
import uz.pdp.inventorymanagementsystem.dto.WarehouseResponseDTO
import uz.pdp.inventorymanagementsystem.model.Warehouse

@Component
class WarehouseMapper(
    @Lazy private val supplierMapper: SupplierMapper
) : BaseMapper<Warehouse, WarehouseResponseDTO> {

    override fun toDTO(entity: Warehouse): WarehouseResponseDTO {
        return WarehouseResponseDTO(
            name = entity.name,
            address = entity.address,
            code = entity.code,
            suppliers = entity.suppliers.map { supplierMapper.toDTO(it) }.toSet()
        ).apply {
            mapBaseFields(entity, this)
        }
    }

    fun toEntity(dto: WarehouseCreateDTO): Warehouse {
        return Warehouse().apply {
            name = dto.name
            address = dto.address
        }
    }
}