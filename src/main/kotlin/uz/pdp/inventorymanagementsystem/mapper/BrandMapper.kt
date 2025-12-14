package uz.pdp.inventorymanagementsystem.mapper

import org.springframework.stereotype.Component
import uz.pdp.inventorymanagementsystem.base.BaseMapper
import uz.pdp.inventorymanagementsystem.dto.BrandResponseDTO
import uz.pdp.inventorymanagementsystem.model.Brand

@Component
class BrandMapper : BaseMapper<Brand, BrandResponseDTO> {

    override fun toDTO(entity: Brand): BrandResponseDTO {
        return BrandResponseDTO(
            name = entity.name,
            country = entity.country
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
}