package uz.pdp.inventorymanagementsystem.repo

import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.Warehouse

interface WarehouseRepo : BaseRepo<Warehouse> {
    fun existsByNameAndDeletedFalse(name: String): Boolean
    fun existsByAddressAndDeletedFalse(address: String?): Boolean
    fun existsByCode(code: String): Boolean

    fun findTopByOrderByCodeDesc(): Warehouse?
    fun findByName(name: String): Warehouse?
    fun findByAddress(address: String?): Warehouse?

    fun findTopByOrderByCreatedAtDesc(): Warehouse?

    fun findByNameAndDeletedFalse(name: String): Warehouse?
    fun findByAddressAndDeletedFalse(name: String): Warehouse?
}