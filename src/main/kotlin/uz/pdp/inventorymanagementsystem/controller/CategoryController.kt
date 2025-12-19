package uz.pdp.inventorymanagementsystem.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uz.pdp.inventorymanagementsystem.dto.CategoryCreateDTO
import uz.pdp.inventorymanagementsystem.dto.CategoryResponseDTO
import uz.pdp.inventorymanagementsystem.dto.CategoryUpdateDTO
import uz.pdp.inventorymanagementsystem.service.CategoryService
import java.util.UUID
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController(
    private val categoryService: CategoryService
) {

    @PostMapping
    fun create(@Valid @RequestBody dto: CategoryCreateDTO): ResponseEntity<CategoryResponseDTO> =
        ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(dto))

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<CategoryResponseDTO> =
        ResponseEntity.ok(categoryService.getById(id))

    @GetMapping
    fun getAll(): ResponseEntity<List<CategoryResponseDTO>> =
        ResponseEntity.ok(categoryService.getAll())

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: CategoryUpdateDTO
    ): ResponseEntity<CategoryResponseDTO> =
        ResponseEntity.ok(categoryService.update(id, dto))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Unit> {
        categoryService.delete(id)
        return ResponseEntity.noContent().build()
    }
}