package uz.pdp.inventorymanagementsystem.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uz.pdp.inventorymanagementsystem.dto.BrandCreateDTO
import uz.pdp.inventorymanagementsystem.dto.BrandResponseDTO
import uz.pdp.inventorymanagementsystem.dto.BrandUpdateDTO
import uz.pdp.inventorymanagementsystem.service.BrandService
import java.util.UUID
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/v1/brands")
class BrandController(
    private val brandService: BrandService
) {

    @PostMapping
    fun create(@Valid @RequestBody dto: BrandCreateDTO): ResponseEntity<BrandResponseDTO> =
        ResponseEntity.status(HttpStatus.CREATED).body(brandService.create(dto))

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<BrandResponseDTO> =
        ResponseEntity.ok(brandService.getById(id))

    @GetMapping
    fun getAll(): ResponseEntity<List<BrandResponseDTO>> =
        ResponseEntity.ok(brandService.getAll())

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: BrandUpdateDTO
    ): ResponseEntity<BrandResponseDTO> =
        ResponseEntity.ok(brandService.update(id, dto))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Unit> {
        brandService.delete(id)
        return ResponseEntity.noContent().build()
    }
}