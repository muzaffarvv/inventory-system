package uz.pdp.inventorymanagementsystem.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import uz.pdp.inventorymanagementsystem.dto.TagCreateDTO
import uz.pdp.inventorymanagementsystem.dto.TagResponseDTO
import uz.pdp.inventorymanagementsystem.dto.TagUpdateDTO
import uz.pdp.inventorymanagementsystem.service.TagService
import java.util.UUID
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/v1/tags")
class TagController(
    private val tagService: TagService
) {

    @PostMapping
    fun create(@Valid @RequestBody dto: TagCreateDTO): ResponseEntity<TagResponseDTO> =
        ResponseEntity.status(HttpStatus.CREATED).body(tagService.create(dto))

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<TagResponseDTO> =
        ResponseEntity.ok(tagService.getById(id))

    @GetMapping
    fun getAll(): ResponseEntity<List<TagResponseDTO>> =
        ResponseEntity.ok(tagService.getAll())

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: TagUpdateDTO
    ): ResponseEntity<TagResponseDTO> =
        ResponseEntity.ok(tagService.update(id, dto))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Unit> {
        tagService.delete(id)
        return ResponseEntity.noContent().build()
    }
}