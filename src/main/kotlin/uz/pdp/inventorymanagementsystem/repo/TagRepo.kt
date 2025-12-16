package uz.pdp.inventorymanagementsystem.repo

import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.Tag

interface TagRepo : BaseRepo<Tag> {
    fun existsByNameAndDeletedFalse (name: String): Boolean
    fun findByNameAndDeletedFalse(name: String): Tag?
}