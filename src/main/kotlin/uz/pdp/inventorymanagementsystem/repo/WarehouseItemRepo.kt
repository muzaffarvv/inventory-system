package uz.pdp.inventorymanagementsystem.repo

import org.springframework.data.jpa.repository.Query
import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.model.WarehouseItem
import java.time.Instant
import java.util.UUID

interface WarehouseItemRepo : BaseRepo<WarehouseItem> {

    // By warehouse
    fun findAllByTransaction_Warehouse_IdAndDeletedFalse(
        warehouseId: UUID
    ): List<WarehouseItem>

    // By transaction
    fun findAllByTransaction_IdAndDeletedFalse(
        transactionId: UUID
    ): List<WarehouseItem>

    // Duplicate check
    fun existsByTransaction_IdAndProduct_IdAndDeletedFalse(
        transactionId: UUID,
        productId: UUID
    ): Boolean

    // Expiration queries
    @Query("""
        SELECT wi FROM WarehouseItem wi
        WHERE wi.expireAt IS NOT NULL
        AND wi.deleted = false
    """)
    fun findAllWithExpiration(): List<WarehouseItem>

    @Query("""
        SELECT wi FROM WarehouseItem wi
        WHERE wi.expireAt IS NOT NULL
        AND wi.expireAt BETWEEN :start AND :end
        AND wi.deleted = false
    """)
    fun findAllExpiringBetween(start: Instant, end: Instant): List<WarehouseItem>

    // Product inventory tracking
    @Query("""
        SELECT wi FROM WarehouseItem wi
        WHERE wi.product.id = :productId
        AND wi.transaction.warehouse.id = :warehouseId
        AND wi.deleted = false
    """)
    fun findAllByProductAndWarehouse(
        productId: UUID,
        warehouseId: UUID
    ): List<WarehouseItem>
}