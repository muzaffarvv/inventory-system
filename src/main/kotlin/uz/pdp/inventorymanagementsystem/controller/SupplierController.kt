package uz.pdp.inventorymanagementsystem.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import uz.pdp.inventorymanagementsystem.dto.SupplierCreateDTO
import uz.pdp.inventorymanagementsystem.dto.SupplierResponseDTO
import uz.pdp.inventorymanagementsystem.dto.SupplierUpdateDTO
import uz.pdp.inventorymanagementsystem.service.SupplierService
import java.util.UUID
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/v1/suppliers")
class SupplierController(
    private val supplierService: SupplierService
) {

    @PostMapping
    fun create(@Valid @RequestBody dto: SupplierCreateDTO): ResponseEntity<SupplierResponseDTO> =
        ResponseEntity.status(HttpStatus.CREATED).body(supplierService.create(dto))

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<SupplierResponseDTO> =
        ResponseEntity.ok(supplierService.getById(id))

    @GetMapping
    fun getAll(): ResponseEntity<List<SupplierResponseDTO>> =
        ResponseEntity.ok(supplierService.getAll())

    @GetMapping("/warehouse/{warehouseId}")
    fun getByWarehouse(@PathVariable warehouseId: UUID): ResponseEntity<Set<SupplierResponseDTO>> =
        ResponseEntity.ok(supplierService.getWarehouseId(warehouseId))

    @GetMapping("/brand/{brandId}")
    fun getByBrand(@PathVariable brandId: UUID): ResponseEntity<Set<SupplierResponseDTO>> =
        ResponseEntity.ok(supplierService.getByBrandId(brandId))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: SupplierUpdateDTO
    ): ResponseEntity<SupplierResponseDTO> =
        ResponseEntity.ok(supplierService.update(id, dto))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Unit> {
        supplierService.delete(id)
        return ResponseEntity.noContent().build()
    }
}