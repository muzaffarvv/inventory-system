package uz.pdp.inventorymanagementsystem.repo

import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.UoM

interface UoMRepo : BaseRepo<UoM> {
    fun findByCodeAndDeletedFalse(code: String): UoM?
    fun existsByCodeAndDeletedFalse(code: String): Boolean

}