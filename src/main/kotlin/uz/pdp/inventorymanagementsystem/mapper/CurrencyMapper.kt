package uz.pdp.inventorymanagementsystem.mapper

import org.springframework.stereotype.Component
import uz.pdp.inventorymanagementsystem.base.BaseMapper
import uz.pdp.inventorymanagementsystem.dto.CurrencyCreateDTO
import uz.pdp.inventorymanagementsystem.dto.CurrencyResponseDTO
import uz.pdp.inventorymanagementsystem.model.Currency

@Component
class CurrencyMapper : BaseMapper<Currency, CurrencyResponseDTO> {

    override fun toDTO(entity: Currency): CurrencyResponseDTO {
        return CurrencyResponseDTO(
            code = entity.code,
            name = entity.name
        ).apply {
            mapBaseFields(entity, this)
        }
    }

    fun toEntity(dto: CurrencyCreateDTO): Currency {
        return Currency().apply {
            code = dto.code
            name = dto.name
        }
    }
}