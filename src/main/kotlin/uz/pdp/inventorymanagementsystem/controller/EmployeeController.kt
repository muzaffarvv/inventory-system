package uz.pdp.inventorymanagementsystem.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import uz.pdp.inventorymanagementsystem.dto.EmployeeCreateDTO
import uz.pdp.inventorymanagementsystem.dto.EmployeeResponseDTO
import uz.pdp.inventorymanagementsystem.dto.EmployeeUpdateDTO
import uz.pdp.inventorymanagementsystem.service.EmployeeService
import java.util.UUID
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/v1/employees")
class EmployeeController(
    private val employeeService: EmployeeService
) {

    @PostMapping
    fun create(@Valid @RequestBody dto: EmployeeCreateDTO): ResponseEntity<EmployeeResponseDTO> =
        ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(dto))

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<EmployeeResponseDTO> =
        ResponseEntity.ok(employeeService.getById(id))

    @GetMapping
    fun getAll(): ResponseEntity<List<EmployeeResponseDTO>> =
        ResponseEntity.ok(employeeService.getAll())

    @GetMapping("/warehouse/{warehouseId}")
    fun getByWarehouse(@PathVariable warehouseId: UUID): ResponseEntity<Set<EmployeeResponseDTO>> =
        ResponseEntity.ok(employeeService.getByWarehouseId(warehouseId))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: EmployeeUpdateDTO
    ): ResponseEntity<EmployeeResponseDTO> =
        ResponseEntity.ok(employeeService.update(id, dto))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Unit> {
        employeeService.delete(id)
        return ResponseEntity.noContent().build()
    }
}