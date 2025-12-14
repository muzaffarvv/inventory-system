package uz.pdp.inventorymanagementsystem.mapper

import org.springframework.stereotype.Component
import uz.pdp.inventorymanagementsystem.base.BaseMapper
import uz.pdp.inventorymanagementsystem.dto.UoMCreateDTO
import uz.pdp.inventorymanagementsystem.dto.UoMResponseDTO
import uz.pdp.inventorymanagementsystem.model.UoM

@Component
class UoMMapper : BaseMapper<UoM, UoMResponseDTO> {

    override fun toDTO(entity: UoM): UoMResponseDTO {
        return UoMResponseDTO(
            code = entity.code,
            name = entity.name
        ).apply {
            mapBaseFields(entity, this)
        }
    }

    fun toEntity(dto: UoMCreateDTO): UoM {
        return UoM().apply {
            code = dto.code
            name = dto.name
        }
    }
}