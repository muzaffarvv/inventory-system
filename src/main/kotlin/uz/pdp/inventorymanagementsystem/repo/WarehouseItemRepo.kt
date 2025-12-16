package uz.pdp.inventorymanagementsystem.repo

import org.springframework.data.jpa.repository.Query
import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.WarehouseItem

interface WarehouseItemRepo : BaseRepo<WarehouseItem>{
    @Query("""
        SELECT wi FROM WarehouseItem wi
        WHERE wi.expireAt IS NOT NULL
        AND wi.deleted = false
    """)
    fun findAllWithExpiration(): List<WarehouseItem>
}