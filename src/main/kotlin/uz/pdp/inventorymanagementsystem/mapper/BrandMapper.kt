package uz.pdp.inventorymanagementsystem.mapper

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import uz.pdp.inventorymanagementsystem.base.BaseMapper
import uz.pdp.inventorymanagementsystem.dto.BrandResponseDTO
import uz.pdp.inventorymanagementsystem.model.Brand

@Component
class BrandMapper(
    @Lazy private val supplierMapper: SupplierMapper
) : BaseMapper<Brand, BrandResponseDTO> {

    override fun toDTO(entity: Brand): BrandResponseDTO {
        return BrandResponseDTO(
            name = entity.name,
            country = entity.country,
            suppliers = entity.suppliers.map { supplierMapper.toDTO(it) }.toSet()
        ).apply {
            mapBaseFields(entity, this)
        }
    }

    fun toEntity(dto: BrandResponseDTO): Brand {
        return Brand().apply {
            name = dto.name
            country = dto.country
        }
    }

    fun toEntityList(dtos: List<BrandResponseDTO>): List<Brand> {
        return dtos.map { toEntity(it) }
    }
}

