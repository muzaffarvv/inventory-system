package uz.pdp.inventorymanagementsystem.repo

import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.Brand

interface BrandRepo : BaseRepo<Brand> {
    fun existsByNameAndDeletedFalse(name: String): Boolean
    fun findByNameAndDeletedFalse(name: String): Brand?
}