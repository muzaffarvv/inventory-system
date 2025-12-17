package uz.pdp.inventorymanagementsystem.repo

import uz.pdp.inventorymanagementsystem.base.BaseRepo
import uz.pdp.inventorymanagementsystem.enums.WarehouseTransactionType
import uz.pdp.inventorymanagementsystem.model.WarehouseTransaction
import java.time.LocalDate
import java.util.UUID

interface WarehouseTransactionRepo : BaseRepo<WarehouseTransaction> {

    // Factory number uniqueness
    fun existsByFactoryNumberAndDeletedFalse(factoryNumber: String): Boolean
    fun findByFactoryNumberAndDeletedFalse(factoryNumber: String): WarehouseTransaction?

    fun findAllByDeletedFalse(): List<WarehouseTransaction>

    // Warehouse filtering
    fun findAllByWarehouse_IdAndDeletedFalse(warehouseId: UUID): List<WarehouseTransaction>

    // Type filtering (INBOUND/OUTBOUND)
    fun findAllByTypeAndDeletedFalse(type: WarehouseTransactionType): List<WarehouseTransaction>

    // Date range queries
    fun findAllByDateBetweenAndDeletedFalse(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<WarehouseTransaction>

    // Combined filters
    fun findAllByWarehouse_IdAndTypeAndDeletedFalse(
        warehouseId: UUID,
        type: WarehouseTransactionType
    ): List<WarehouseTransaction>

    // Get latest transaction (for code generation if needed)
    fun findTopByOrderByCreatedAtDesc(): WarehouseTransaction?
}