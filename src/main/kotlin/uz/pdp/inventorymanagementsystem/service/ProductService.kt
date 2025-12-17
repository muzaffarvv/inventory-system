package uz.pdp.inventorymanagementsystem.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import uz.pdp.inventorymanagementsystem.base.BaseServiceImpl
import uz.pdp.inventorymanagementsystem.dto.ProductCreateDTO
import uz.pdp.inventorymanagementsystem.dto.ProductResponseDTO
import uz.pdp.inventorymanagementsystem.dto.ProductUpdateDTO
import uz.pdp.inventorymanagementsystem.enums.Status
import uz.pdp.inventorymanagementsystem.exception.ProductNotFoundException
import uz.pdp.inventorymanagementsystem.exception.ValidationException
import uz.pdp.inventorymanagementsystem.mapper.ProductMapper
import uz.pdp.inventorymanagementsystem.model.Brand
import uz.pdp.inventorymanagementsystem.model.Category
import uz.pdp.inventorymanagementsystem.model.Product
import uz.pdp.inventorymanagementsystem.model.Tag
import uz.pdp.inventorymanagementsystem.model.UoM
import uz.pdp.inventorymanagementsystem.repo.ProductRepo
import uz.pdp.inventorymanagementsystem.utils.CodeGenerator
import java.util.UUID

@Service
class ProductService(
    override val repository: ProductRepo,
    override val mapper: ProductMapper,
    private val brandService: BrandService,
    private val categoryService: CategoryService,
    private val tagService: TagService,
    private val uoMService: UoMService,
    private val fileStorageService: FileStorageService
) : BaseServiceImpl<Product, ProductCreateDTO, ProductUpdateDTO, ProductResponseDTO, ProductMapper, ProductRepo>(
    repository, mapper
) {



    @Transactional
    fun createWithFiles(
        dto: ProductCreateDTO,
        files: List<MultipartFile>?
    ): ProductResponseDTO {

        validateCreate(dto)

        val product = convertCreateDtoToEntity(dto)
        saveAndRefresh(product)

        files
            ?.takeIf { it.isNotEmpty() }
            ?.let { attachFiles(product, it) }

        return mapper.toDTO(product)
    }

    private fun attachFiles(product: Product, files: List<MultipartFile>) {
        val savedFiles = fileStorageService.save(files)
        savedFiles.forEach { it.product = product }
    }

    private fun replaceFiles(product: Product, newFiles: List<MultipartFile>) {
        fileStorageService.deleteAllByProduct(product.id!!)
        attachFiles(product, newFiles)
    }

    @Transactional
    fun updateWithFiles(
        id: UUID,
        dto: ProductUpdateDTO,
        files: List<MultipartFile>?
    ): ProductResponseDTO {

        val product = getByIdOrThrow(id)

        validateUpdate(id, dto)
        updateEntityFromDto(product, dto)
        saveAndRefresh(product)

        files
            ?.takeIf { it.isNotEmpty() }
            ?.let {
                fileStorageService.deleteAllByProduct(product.id!!)
                val savedFiles = fileStorageService.save(it)
                savedFiles.forEach { file -> file.product = product }
            }

        return mapper.toDTO(product)
    }

    /* ===================== CRUD ===================== */

    override fun create(dto: ProductCreateDTO): ProductResponseDTO {
        validateCreate(dto)
        val product = convertCreateDtoToEntity(dto)
        return mapper.toDTO(saveAndRefresh(product))
    }

    override fun getById(id: UUID): ProductResponseDTO =
        mapper.toDTO(getByIdOrThrow(id))

    override fun getAll(): List<ProductResponseDTO> =
        repository.findAllByDeletedFalse().map(mapper::toDTO)

    override fun update(id: UUID, dto: ProductUpdateDTO): ProductResponseDTO {
        val product = getByIdOrThrow(id)
        validateUpdate(id, dto)
        updateEntityFromDto(product, dto)
        return mapper.toDTO(saveAndRefresh(product))
    }

    override fun delete(id: UUID) {
        getByIdOrThrow(id)
        repository.trash(id)
    }

    /* ===================== HELPERS ===================== */

    fun getByIdOrThrow(id: UUID): Product =
        getEntityOrNull(id) ?: throw ProductNotFoundException(
            "Product with id '$id' not found"
        )

    fun getActive(id: UUID): Product =
        getByIdOrThrow(id).apply {
            if (status != Status.ACTIVE) throw ValidationException(
                mapOf("product" to "Product with id '$id' is INACTIVE.")
            )
        }

    private fun getCodeLastNumber(): Int {
        val lastCode = repository.findTopByOrderByCreatedAtDesc()?.productCode ?: return 0
        return Regex("""\d+$""").find(lastCode)?.value?.toInt() ?: 0
    }

    /* ===================== FETCH & VALIDATE ===================== */
    // shuni bitta qilsam muammo chiqardi
    private fun fetchAndValidateEntities(dto: ProductCreateDTO): ActiveEntities {
        val errors = mutableMapOf<String, String>()

        val category = runCatching { categoryService.getActive(dto.categoryId) }
            .getOrElse { errors["category"] = "Category with id '${dto.categoryId}' not found or inactive"; null }

        val brand = runCatching { brandService.getActive(dto.brandId) }
            .getOrElse { errors["brand"] = "Brand with id '${dto.brandId}' not found or inactive"; null }

        val uom = dto.uomId.let {
            runCatching { uoMService.getActive(it) }
                .getOrElse { errors["uoM"] = "UoM with id '$it' not found or inactive"; null }
        }

        val tags = dto.tagIds?.mapNotNull { tagId ->
            runCatching { tagService.getActive(tagId) }
                .getOrElse { errors["tag_$tagId"] = "Tag with id '$tagId' not found or inactive"; null }
        }?.toMutableSet() ?: mutableSetOf()

        if (errors.isNotEmpty()) throw ValidationException(errors)
        return ActiveEntities(category!!, brand!!, uom, tags)
    }

    private fun fetchAndValidateEntities(dto: ProductUpdateDTO): ActiveEntities {
        val errors = mutableMapOf<String, String>()

        val category = runCatching { categoryService.getActive(dto.categoryId) }
            .getOrElse { errors["category"] = "Category with id '${dto.categoryId}' not found or inactive"; null }

        val brand = runCatching { brandService.getActive(dto.brandId) }
            .getOrElse { errors["brand"] = "Brand with id '${dto.brandId}' not found or inactive"; null }

        val uom = dto.uomId.let {
            runCatching { uoMService.getActive(it) }
                .getOrElse { errors["uoM"] = "UoM with id '$it' not found or inactive"; null }
        }

        val tags = dto.tagIds?.mapNotNull { tagId ->
            runCatching { tagService.getActive(tagId) }
                .getOrElse { errors["tag_$tagId"] = "Tag with id '$tagId' not found or inactive"; null }
        }?.toMutableSet() ?: mutableSetOf()

        if (errors.isNotEmpty()) throw ValidationException(errors)
        return ActiveEntities(category!!, brand!!, uom, tags)
    }

    data class ActiveEntities(
        val category: Category,
        val brand: Brand,
        val uom: UoM?,
        val tags: MutableSet<Tag>
    )

    /* ===================== MAPPING (Override Methods) ===================== */

    override fun convertCreateDtoToEntity(dto: ProductCreateDTO): Product {
        val (category, brand, uom, tags) = fetchAndValidateEntities(dto)
        val existing = repository.findByNameAndDeletedFalse(dto.name)

        return existing?.apply {
            description = dto.description
            price = dto.price
            profit = dto.profit
            this.brand = brand
            this.category = category
            this.uom = uom
            this.tags = tags
            status = Status.ACTIVE
        } ?: Product().apply {
            name = dto.name
            description = dto.description
            price = dto.price
            profit = dto.profit
            productCode = CodeGenerator.forProduct(getCodeLastNumber())
            this.brand = brand
            this.category = category
            this.uom = uom
            this.tags = tags
            status = Status.ACTIVE
        }
    }

    override fun updateEntityFromDto(entity: Product, dto: ProductUpdateDTO) {
        val (category, brand, uom, tags) = fetchAndValidateEntities(dto)

        dto.name?.let { entity.name = it }
        dto.description?.let { entity.description = it }
        dto.price?.let { entity.price = it }
        dto.profit?.let { entity.profit = it }

        entity.brand = brand
        entity.category = category
        entity.uom = uom
        entity.tags = tags
    }

    /* ===================== VALIDATION ===================== */

    override fun validateCreate(dto: ProductCreateDTO) {
        fetchAndValidateEntities(dto)
        val existing = repository.findByNameAndDeletedFalse(dto.name)
        if (existing != null && existing.status != Status.ACTIVE) {
            throw ValidationException(mapOf("name" to "Product with name '${dto.name}' exists but is not ACTIVE"))
        }
    }

    override fun validateUpdate(id: UUID, dto: ProductUpdateDTO) {
        getByIdOrThrow(id)
        fetchAndValidateEntities(dto) // unified validation for update
    }
}
