package uz.pdp.inventorymanagementsystem.service

import org.springframework.stereotype.Service
import uz.pdp.inventorymanagementsystem.base.BaseServiceImpl
import uz.pdp.inventorymanagementsystem.dto.CategoryCreateDTO
import uz.pdp.inventorymanagementsystem.dto.CategoryResponseDTO
import uz.pdp.inventorymanagementsystem.dto.CategoryUpdateDTO
import uz.pdp.inventorymanagementsystem.enums.Status
import uz.pdp.inventorymanagementsystem.exception.CategoryNotFoundException
import uz.pdp.inventorymanagementsystem.exception.ValidationException
import uz.pdp.inventorymanagementsystem.mapper.CategoryMapper
import uz.pdp.inventorymanagementsystem.model.Category
import uz.pdp.inventorymanagementsystem.repo.CategoryRepo
import java.util.UUID

@Service
class CategoryService(
    override val repository: CategoryRepo,
    override val mapper: CategoryMapper
) : BaseServiceImpl<
        Category,
        CategoryCreateDTO,
        CategoryUpdateDTO,
        CategoryResponseDTO,
        CategoryMapper,
        CategoryRepo
        >(repository, mapper) {

    override fun create(dto: CategoryCreateDTO): CategoryResponseDTO {
        validateCreate(dto)

        val category = convertCreateDtoToEntity(dto)
        val saved = saveAndRefresh(category)

        return mapper.toDTO(saved)
    }


    override fun getById(id: UUID): CategoryResponseDTO {
        val category = getByIdOrThrow(id) // (active || inactive)
        return mapper.toDTO(category)
    }

    override fun update(id: UUID, dto: CategoryUpdateDTO): CategoryResponseDTO {
        val category = getActive(id)

        validateUpdate(id, dto)
        updateEntityFromDto(category, dto)

        val updated = saveAndRefresh(category)
        return mapper.toDTO(updated)
    }

    override  fun delete(id: UUID) {
        getByIdOrThrow(id)
        validateDelete(id)
        repository.trash(id)
    }

    /* ===================== HELPERS ===================== */
    fun getByIdOrThrow(id: UUID): Category =
        getEntityOrNull(id) ?: throw CategoryNotFoundException(
            "Category with id '$id' not found."
        )

    fun getActive(id: UUID): Category =
        getByIdOrThrow(id).apply {
            if (status != Status.ACTIVE) {
                throw ValidationException(
                    mapOf(
                        "category" to "Category with '$id' is INACTIVE."
                    )
                )
            }
        }


    /* ===================== MAPPING ===================== */

    override fun convertCreateDtoToEntity(dto: CategoryCreateDTO): Category {
        return Category().apply {
            name = dto.name
            // Agar parentId null bo'sa null, bo'masa parent o'rnatiladi
            parent = dto.parentId?.let { Category().apply { id = it } }
            isLast = true
        }
    }

    override fun updateEntityFromDto(
        entity: Category,
        dto: CategoryUpdateDTO
    ) {
        dto.name?.let { entity.name = it }

        entity.parent = dto.parentId?.let { Category().apply { id = it } }

        if (dto.parentId == null) {
            entity.parent = null
        }
    }

    /* ===================== VALIDATION ===================== */

    override fun validateCreate(dto: CategoryCreateDTO) {

        dto.name.takeIf { repository.existsByNameAndDeletedFalse(it) }
            ?.let { name ->
                throw ValidationException(
                    mapOf(
                        "name" to "Category with name '$name' already exists."
                    )
                )
            }

        // Parent active + isLast update
        dto.parentId?.let { parentId ->
            val parent = getActive(parentId)

            // parent ozod
            if (parent.isLast) {
                parent.isLast = false
                repository.save(parent)
            }
        }
    }

    override fun validateUpdate(id: UUID, dto: CategoryUpdateDTO) {
        val current = getByIdOrThrow(id)

        dto.parentId?.let { parentId ->
            getActive(parentId)
        }

        dto.name?.let { name ->
            repository.findByNameAndDeletedFalse(name)
                ?.takeIf { it.id != current.id }
                ?.let { category ->
                    throw ValidationException(
                        mapOf(
                            "name" to "Category with name '$name' already exists."
                        )
                    )
                }
        }
    }

    override fun validateDelete(id: UUID) {
        val category = getByIdOrThrow(id)
        val parent = category.parent

        // Children bo'lsa, ularni o'chirilgan category parentiga o'tkazish
        category.children.forEach { child ->
            child.parent = parent
            repository.save(child)
        }

        // Agar parent bor bo'lsa va
        // o'chirilayotgan category parentning oxirgi bolasi bo'lsa
        parent?.let {
            val siblingsCount = repository.countByParentIdAndDeletedFalse(it.id!!)
            if (siblingsCount == 1L) {
                it.isLast = true
                repository.save(it)
            }
        }
    }



}