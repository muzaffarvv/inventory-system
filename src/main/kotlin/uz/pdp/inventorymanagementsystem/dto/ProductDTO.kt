package uz.pdp.inventorymanagementsystem.dto

import uz.pdp.inventorymanagementsystem.base.BaseDTO
import java.math.BigDecimal
import java.util.UUID

data class ProductResponseDTO(
    var name: String = "",
    var description: String = "",
    var weight: Double? = null,
    var price: BigDecimal = BigDecimal.ZERO,
    var profit: Int? = null,
    var productCode: String = "",
    var brandResponseDTO: BrandResponseDTO? = null,
    var categoryResponseDTO: CategoryResponseDTO? = null,
    var uoMResponseDTO: UoMResponseDTO? = null,
    var currencyResponseDTO: CurrencyResponseDTO? = null,
    var tags: Set<TagResponseDTO> = emptySet()
) : BaseDTO()

data class ProductCreateDTO(
    var name: String,
    var description: String = "",
    var weight: Double? = null,
    var price: BigDecimal,
    var profit: Int? = null,
    var brandId: UUID? = null,
    var categoryId: UUID? = null,
    var uomId: UUID? = null,
    var currencyId: UUID? = null,
    var tagIds: Set<UUID> = emptySet()
)

data class ProductUpdateDTO(
    var name: String? = null,
    var description: String? = null,
    var weight: Double? = null,
    var price: BigDecimal? = null,
    var profit: Int? = null,
    var brandId: UUID? = null,
    var categoryId: UUID? = null,
    var uomId: UUID? = null,
    var currencyId: UUID? = null,
    var tagIds: Set<UUID>? = null
)