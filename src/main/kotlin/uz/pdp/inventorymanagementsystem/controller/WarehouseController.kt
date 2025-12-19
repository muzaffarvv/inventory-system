package uz.pdp.inventorymanagementsystem.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uz.pdp.inventorymanagementsystem.dto.WarehouseCreateDTO
import uz.pdp.inventorymanagementsystem.dto.WarehouseResponseDTO
import uz.pdp.inventorymanagementsystem.dto.WarehouseUpdateDTO
import uz.pdp.inventorymanagementsystem.service.WarehouseService
import java.util.UUID
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/v1/warehouses")
class WarehouseController(
    private val warehouseService: WarehouseService
) {

    @PostMapping
    fun create(@Valid @RequestBody dto: WarehouseCreateDTO): ResponseEntity<WarehouseResponseDTO> =
        ResponseEntity.status(HttpStatus.CREATED).body(warehouseService.create(dto))

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<WarehouseResponseDTO> =
        ResponseEntity.ok(warehouseService.getById(id))

    @GetMapping
    fun getAll(): ResponseEntity<List<WarehouseResponseDTO>> =
        ResponseEntity.ok(warehouseService.getAll())

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: WarehouseUpdateDTO
    ): ResponseEntity<WarehouseResponseDTO> =
        ResponseEntity.ok(warehouseService.update(id, dto))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Unit> {
        warehouseService.delete(id)
        return ResponseEntity.noContent().build()
    }
}