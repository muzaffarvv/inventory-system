package uz.pdp.inventorymanagementsystem.repo

import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.Brand
import java.util.UUID

interface BrandRepo : BaseRepo<Brand> {
    fun existsByNameAndDeletedFalse(name: String): Boolean
    fun findByName(name: String): Brand?

    fun findByNameAndDeletedFalse(name: String): Brand?

}