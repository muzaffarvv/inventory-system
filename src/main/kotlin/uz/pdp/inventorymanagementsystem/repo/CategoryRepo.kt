package uz.pdp.inventorymanagementsystem.repo

import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.Category
import java.util.UUID

interface CategoryRepo : BaseRepo<Category> {
    fun existsByNameAndDeletedFalse(name: String): Boolean

    fun findByNameAndDeletedFalse(name: String): Category?

    fun countByParentIdAndDeletedFalse(id: UUID): Long
}