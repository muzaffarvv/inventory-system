package uz.pdp.inventorymanagementsystem.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uz.pdp.inventorymanagementsystem.dto.*
import uz.pdp.inventorymanagementsystem.enums.WarehouseTransactionType
import uz.pdp.inventorymanagementsystem.service.WarehouseTransactionService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/warehouse-transactions")
class WarehouseTransactionController(
    private val transactionService: WarehouseTransactionService
) {
    @PostMapping
    fun create(@Valid @RequestBody dto: WarehouseTransactionCreateDTO): ResponseEntity<WarehouseTransactionResponseDTO> =
        ResponseEntity.status(HttpStatus.CREATED).body(transactionService.create(dto))

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody dto: WarehouseTransactionUpdateDTO): ResponseEntity<WarehouseTransactionResponseDTO> =
        ResponseEntity.ok(transactionService.update(id, dto))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Unit> {
        transactionService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<WarehouseTransactionResponseDTO> =
        ResponseEntity.ok(transactionService.getById(id))

    @GetMapping
    fun getAll(): ResponseEntity<List<WarehouseTransactionResponseDTO>> =
        ResponseEntity.ok(transactionService.getAll())

    @GetMapping("/warehouse/{warehouseId}")
    fun getByWarehouse(@PathVariable warehouseId: UUID): ResponseEntity<List<WarehouseTransactionResponseDTO>> =
        ResponseEntity.ok(transactionService.getByWarehouseId(warehouseId))

    @GetMapping("/type/{type}")
    fun getByType(@PathVariable type: WarehouseTransactionType): ResponseEntity<List<WarehouseTransactionResponseDTO>> =
        ResponseEntity.ok(transactionService.getByType(type))
}
