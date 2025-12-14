package uz.pdp.inventorymanagementsystem.dto

import uz.pdp.inventorymanagementsystem.base.BaseDTO
import uz.pdp.inventorymanagementsystem.enums.WarehouseTransactionType
import java.time.LocalDate
import java.util.UUID

data class WarehouseTransactionResponseDTO(
    var date: LocalDate ?= LocalDate.now(),
    var description: String? = null,
    var type: WarehouseTransactionType? = null,
    var factoryNumber: String = "",
    var warehouseResponseDTO: WarehouseResponseDTO? = null,
    var items: List<WarehouseItemResponseDTO> = emptyList()
) : BaseDTO()

data class WarehouseTransactionCreateDTO(
    var date: LocalDate = LocalDate.now(),
    var description: String? = null,
    var type: WarehouseTransactionType,
    var factoryNumber: String,
    var warehouseId: UUID,
    var items: List<WarehouseItemCreateDTO>
)

data class WarehouseTransactionUpdateDTO(
    var date: LocalDate? = null,
    var description: String? = null,
    var type: WarehouseTransactionType? = null,
    var factoryNumber: String? = null,
    var warehouseId: UUID? = null
)