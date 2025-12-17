package uz.pdp.inventorymanagementsystem.mapper

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import uz.pdp.inventorymanagementsystem.base.BaseMapper
import uz.pdp.inventorymanagementsystem.dto.ProductCreateDTO
import uz.pdp.inventorymanagementsystem.dto.ProductResponseDTO
import uz.pdp.inventorymanagementsystem.model.*

@Component
class ProductMapper(
    @Lazy private val brandMapper: BrandMapper,
    private val uomMapper: UoMMapper,
    private val categoryMapper: CategoryMapper,
    private val tagMapper: TagMapper
) : BaseMapper<Product, ProductResponseDTO> {

    override fun toDTO(entity: Product): ProductResponseDTO {
        return ProductResponseDTO(
            name = entity.name,
            description = entity.description,
            price = entity.price,
            profit = entity.profit,
            productCode = entity.productCode,
            brandResponseDTO = entity.brand?.let { brandMapper.toDTO(it) },
            categoryResponseDTO = entity.category?.let { categoryMapper.toDTO(it) },
            uoMResponseDTO = entity.uom?.let { uomMapper.toDTO(it) },
            tags = entity.tags.map { tagMapper.toDTO(it) }.toSet()
        ).apply {
            mapBaseFields(entity, this)
        }
    }

    fun toEntity(
        dto: ProductCreateDTO,
        brand: Brand?,
        category: Category?,
        uom: UoM?,
        tags: Set<Tag>
    ): Product {
        return Product().apply {
            name = dto.name
            description = dto.description
            price = dto.price
            profit = dto.profit

            this.brand = brand
            this.category = category
            this.uom = uom
            this.tags.addAll(tags)
        }
    }

}