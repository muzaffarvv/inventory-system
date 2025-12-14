package uz.pdp.inventorymanagementsystem.mapper

import org.springframework.stereotype.Component
import uz.pdp.inventorymanagementsystem.base.BaseMapper
import uz.pdp.inventorymanagementsystem.dto.SupplierCreateDTO
import uz.pdp.inventorymanagementsystem.dto.SupplierResponseDTO
import uz.pdp.inventorymanagementsystem.model.Brand
import uz.pdp.inventorymanagementsystem.model.Supplier

@Component
class SupplierMapper(
    private val brandMapper: BrandMapper
) : BaseMapper<Supplier, SupplierResponseDTO> {

    override fun toDTO(entity: Supplier): SupplierResponseDTO {
        return SupplierResponseDTO(
            name = entity.name,
            phone = entity.phone,
            brands = entity.brands.map { brandMapper.toDTO(it) }.toSet()
        ).apply {
            mapBaseFields(entity, this)
        }
    }

    fun toEntity(dto: SupplierCreateDTO, brands: Set<Brand>): Supplier {
        return Supplier().apply {
            name = dto.name
            phone = dto.phone
            this.brands = brands.toMutableSet()
        }
    }
}