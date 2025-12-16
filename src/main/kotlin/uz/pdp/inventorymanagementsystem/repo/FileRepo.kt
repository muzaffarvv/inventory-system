package uz.pdp.inventorymanagementsystem.repo

import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.File
import java.util.UUID

interface FileRepo : BaseRepo<File> {
    fun findByKeyName(keyName: String): File?
    fun findAllByProductId(productId: UUID): List<File>
}