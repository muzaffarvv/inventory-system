package uz.pdp.inventorymanagementsystem.mapper

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import uz.pdp.inventorymanagementsystem.base.BaseMapper
import uz.pdp.inventorymanagementsystem.dto.SupplierCreateDTO
import uz.pdp.inventorymanagementsystem.dto.SupplierResponseDTO
import uz.pdp.inventorymanagementsystem.model.Brand
import uz.pdp.inventorymanagementsystem.model.Supplier
import uz.pdp.inventorymanagementsystem.model.Warehouse

@Component
class SupplierMapper(
    @Lazy
    private val brandMapper: BrandMapper,
    @Lazy private val warehouseMapper: WarehouseMapper
) : BaseMapper<Supplier, SupplierResponseDTO> {

    override fun toDTO(entity: Supplier): SupplierResponseDTO {
        return SupplierResponseDTO(
            about = entity.about,
            phone = entity.phone,
            brand = entity.brand?.let { brandMapper.toDTO(it) },
            warehouse = entity.warehouse?.let { warehouseMapper.toDTO(it) }

        ).apply {
            mapBaseFields(entity, this)
        }
    }

    fun toEntity(dto: SupplierCreateDTO, brand: Brand, warehouse: Warehouse): Supplier {
        return Supplier().apply {
            about = dto.about
            phone = dto.phone
            this.brand = brand
            this.warehouse = warehouse
        }
    }
}