package uz.pdp.inventorymanagementsystem.repo

import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.Supplier
import java.util.UUID

interface SupplierRepo : BaseRepo<Supplier> {
    fun existsByPhoneAndDeletedFalse(phone: String): Boolean

    fun findByPhone(phone: String): Supplier?

    fun findSuppliersByBrandIdAndDeletedFalse(brandId: UUID): Set<Supplier>
    fun findSuppliersByWarehouseIdAndDeletedFalse(brandId: UUID): Set<Supplier>
    fun findSuppliersByWarehouseId(warehouseId: UUID): Set<Supplier>

    fun findByPhoneAndDeletedFalse(phone: String): Supplier?

}