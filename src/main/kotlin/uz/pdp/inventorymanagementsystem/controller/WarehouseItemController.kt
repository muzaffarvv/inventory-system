package uz.pdp.inventorymanagementsystem.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uz.pdp.inventorymanagementsystem.dto.*
import uz.pdp.inventorymanagementsystem.service.WarehouseItemService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/warehouse-items")
class WarehouseItemController(
    private val warehouseItemService: WarehouseItemService
) {

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<WarehouseItemResponseDTO> =
        ResponseEntity.ok(warehouseItemService.getById(id))

    @GetMapping
    fun getAll(): ResponseEntity<List<WarehouseItemResponseDTO>> =
        ResponseEntity.ok(warehouseItemService.getAll())

    @GetMapping("/warehouse/{warehouseId}")
    fun getByWarehouse(@PathVariable warehouseId: UUID): ResponseEntity<Set<WarehouseItemResponseDTO>> =
        ResponseEntity.ok(warehouseItemService.getByWarehouseId(warehouseId))

    @GetMapping("/transaction/{transactionId}")
    fun getByTransaction(@PathVariable transactionId: UUID): ResponseEntity<List<WarehouseItemResponseDTO>> =
        ResponseEntity.ok(warehouseItemService.getByTransactionId(transactionId))

    @GetMapping("/expiring")
    fun getExpiringSoon(@RequestParam(defaultValue = "30") days: Int): ResponseEntity<List<WarehouseItemResponseDTO>> =
        ResponseEntity.ok(warehouseItemService.getExpiringSoon(days))
}
