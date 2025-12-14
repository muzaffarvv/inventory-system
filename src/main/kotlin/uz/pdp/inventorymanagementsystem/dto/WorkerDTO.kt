package uz.pdp.inventorymanagementsystem.dto

import uz.pdp.inventorymanagementsystem.base.BaseDTO
import java.util.UUID

data class WorkerResponseDTO(
    var firstName: String = "",
    var lastName: String = "",
    var phone: String = "",
    var workerCode: String = "",
    var warehouseResponseDTO: WarehouseResponseDTO? = null
) : BaseDTO()

data class WorkerCreateDTO(
    var firstName: String,
    var lastName: String,
    var phone: String,
    var password: String,
    var warehouseId: UUID
)

data class WorkerUpdateDTO(
    var firstName: String? = null,
    var lastName: String? = null,
    var phone: String? = null,
    var password: String? = null,
    var warehouseId: UUID? = null
)